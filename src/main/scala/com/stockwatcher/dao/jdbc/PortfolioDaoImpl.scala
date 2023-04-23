package com.stockwatcher.dao.jdbc

import com.stockwatcher.HelperUtils
import com.stockwatcher.dao.PortfolioDao
import com.stockwatcher.model.{Portfolio, PortfolioStock, Status}
import com.typesafe.scalalogging.Logger
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.{BeanPropertySqlParameterSource, MapSqlParameterSource, NamedParameterJdbcTemplate, SqlParameterSource, SqlParameterSourceUtils}
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

import scala.jdk.CollectionConverters._

@Repository
class PortfolioDaoImpl(jdbcTemplate: NamedParameterJdbcTemplate) extends PortfolioDao {

  private val logger = Logger(classOf[PortfolioDaoImpl])

  private val INSERT_PORTFOLIO: String = "INSERT INTO stock.portfolio(name, description, created_on, updated_on, status) " +
    "VALUES (:name, :description, current_timestamp(), current_timestamp(), 1)";

  private val GET_PORTFOLIO: String = "SELECT id, name, description, created_on, updated_on, status FROM stock.portfolio WHERE id = :id";

  private val GET_PORTFOLIO_SYMBOLS: String = "SELECT symbol, portfolio_id, quantity, comment, created_on, updated_on FROM stock.stocktoportfolio WHERE portfolio_id = :portfolio_id";

  private val INSERT_PORTFOLIO_STOCK: String = "INSERT INTO stock.stocktoportfolio(symbol, portfolio_id, quantity, comment, created_on, updated_on) VALUES(:symbol, :portfolioId, :quantity, :comment, current_timestamp(), current_timestamp())";

  private val PORTFOLIO_ROW_MAPPER: RowMapper[Portfolio] = (resultSet, rowNum) => {
    Portfolio(
      id = resultSet.getInt("id"),
      name = resultSet.getString("name"),
      description = resultSet.getString("description"),
      stocks = null,
      change = 0,
      createdOn = HelperUtils.toEpochMilli(resultSet.getTimestamp("created_on")).getOrElse(-1),
      updatedOn = HelperUtils.toEpochMilli(resultSet.getTimestamp("updated_on")).getOrElse(-1),
      status = Status(resultSet.getInt("status"))
    )
  }

  private val PORTFOLIO_STOCK_ROW_MAPPER: RowMapper[PortfolioStock] = (resultSet, rowNum) => {
    PortfolioStock(
      portfolioId = resultSet.getInt("portfolio_id"),
      symbol = resultSet.getString("symbol"),
      quantity = resultSet.getInt("quantity"),
      comment = resultSet.getString("comment"),
      value = 0,
      change = 0,
      quote = null,
      createdOn = HelperUtils.toEpochMilli(resultSet.getTimestamp("created_on")).getOrElse(-1),
      updatedOn = HelperUtils.toEpochMilli(resultSet.getTimestamp("updated_on")).getOrElse(-1)
    )
  }

  override def get(id: Int): Option[Portfolio] = {
    try {
      val portfolio = jdbcTemplate.queryForObject(GET_PORTFOLIO, new MapSqlParameterSource("id", id),
        PORTFOLIO_ROW_MAPPER)
      val stocks: List[PortfolioStock] = getPortfolioStocks(id)
      Option(portfolio.copy(stocks = stocks))
    } catch {
      case e: EmptyResultDataAccessException =>
        logger.warn("Cannot find portfolio with id=[{}]", id)
        Option.empty
    }
  }

  override def create(portfolio: Portfolio): Portfolio = {
    val keyHolder = new GeneratedKeyHolder()
    val params = new BeanPropertySqlParameterSource(portfolio)
    jdbcTemplate.update(INSERT_PORTFOLIO, params, keyHolder)

    val portfolioId = keyHolder.getKeyAs(classOf[Integer])
    createPortfolioStocks(portfolioId, portfolio.stocks)

    get(portfolioId).getOrElse(throw new RuntimeException("Cannot fetch the created portfolio"))
  }

  private def getPortfolioStocks(portfolioId: Int): List[PortfolioStock] = {
    jdbcTemplate.query(GET_PORTFOLIO_SYMBOLS, new MapSqlParameterSource("portfolio_id", portfolioId),
      PORTFOLIO_STOCK_ROW_MAPPER).asScala.toList
  }

  private def createPortfolioStocks(portfolioId: Int, stocks: List[PortfolioStock]): Unit = {
    val stocksWithIds = stocks.map(_.copy(portfolioId = portfolioId))
    val sqlParams: Array[SqlParameterSource] = SqlParameterSourceUtils.createBatch(stocksWithIds.asJava)
    jdbcTemplate.batchUpdate(INSERT_PORTFOLIO_STOCK, sqlParams)
  }
}
