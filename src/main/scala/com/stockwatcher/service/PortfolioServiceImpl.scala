package com.stockwatcher.service
import com.stockwatcher.HelperUtils
import com.stockwatcher.dao.PortfolioDao
import com.stockwatcher.model.{Portfolio, PortfolioStock, Quote}
import com.typesafe.scalalogging.Logger
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service

import scala.util.Try

@Service
class PortfolioServiceImpl(stockService: StockService, portfolioDao: PortfolioDao) extends PortfolioService {

  private val logger = Logger(classOf[PortfolioServiceImpl])

  /**
   * @inheritdoc
   */
  override def get(id: Int): Portfolio = portfolioDao.get(id)
    .map(enrichPortfolio)
    .getOrElse(throw new IllegalArgumentException(s"Cannot find portfolio with id $id"))

  /**
   * @inheritdoc
   */
  override def create(portfolio: Portfolio): Portfolio = {
    validatePortfolio(portfolio)
    portfolioDao.create(portfolio)
  }

  /**
   * @inheritdoc
   */
  override def update(portfolio: Portfolio): Portfolio = ???

  /**
   * @inheritdoc
   */
  override def delete(id: Int): Unit = ???

  private def validatePortfolio(portfolio: Portfolio): Unit = {
    // validate the name
    if (StringUtils.isBlank(portfolio.name)) {
      throw new IllegalArgumentException("Portfolio name must be provided")
    }

    // validate the symbols by fetching from stock service
    Option(portfolio.stocks) match {
      case Some(symbolList) =>
        val invalidSymbolsConfigs = symbolList.filter(portfolioSymbol =>
          StringUtils.isBlank(portfolioSymbol.symbol) || portfolioSymbol.quantity <= 0)
        if (invalidSymbolsConfigs.nonEmpty) {
          throw new IllegalArgumentException("Invalid symbol(s) found. Ensure all symbols are not null/blank with quantity " +
            "greater than 0")
        }
        // validate symbols
        val invalidSymbols = symbolList
          .map(portfolioSymbol => portfolioSymbol.symbol)
          .filter(r => Try(stockService.getQuote(r)).isFailure)
        if (invalidSymbols.nonEmpty) {
          throw new IllegalArgumentException(s"Following symbols are not valid: ${invalidSymbols.mkString(", ")}")
        }
      case None => logger.info("No symbols specified - skipping symbol checks")
    }
  }

  private def enrichPortfolio(portfolio: Portfolio): Portfolio = {
    val enrichedStocks = Option(portfolio.stocks).getOrElse(List()).map(enrichPortfolioStock)
    // calculate the overall change
    val overallChange = HelperUtils.round(enrichedStocks.map(_.change).sum, 2)
    val totalValue = HelperUtils.round(enrichedStocks.map(_.value).sum, 2)
    portfolio.copy(stocks = enrichedStocks, value = totalValue, change = overallChange)
  }

  private def enrichPortfolioStock(portfolioStock: PortfolioStock): PortfolioStock = {
    Option(portfolioStock).map(portfolioStock => {
      val quote: Quote = stockService.getQuote(portfolioStock.symbol)
      val totalChange = HelperUtils.round(quote.change * portfolioStock.quantity, 2)
      val totalValue = HelperUtils.round(quote.price * portfolioStock.quantity, 2)
      portfolioStock.copy(value = totalValue, change = totalChange, quote = quote)
    }).orNull
  }
}
