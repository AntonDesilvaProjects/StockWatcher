package com.stockwatcher.service

import com.stockwatcher.model.Quote

trait StockService {

  /**
   * Gets the current quote for a stock symbol
   * @param symbol stock symbol to fetch quote information on
   * @return Quote
   */
  def getQuote(symbol: String): Quote
}
