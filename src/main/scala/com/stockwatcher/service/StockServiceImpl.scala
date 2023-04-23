package com.stockwatcher.service

import com.stockwatcher.dao.alphavantage.AlphaVantageStockApi
import com.stockwatcher.model.Quote
import com.typesafe.scalalogging.Logger
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service

@Service
class StockServiceImpl(stockApi: AlphaVantageStockApi) extends StockService {

  private val logger = Logger(classOf[StockServiceImpl])

  /**
   * @inheritdoc
   */
  override def getQuote(symbol: String): Quote = {
    logger.info(s"Getting the quote for symbol=[$symbol]")
    if (StringUtils.isEmpty(symbol)) {
      throw new IllegalArgumentException("Symbol must be non-empty")
    }
    stockApi.getQuote(symbol)
  }
}
