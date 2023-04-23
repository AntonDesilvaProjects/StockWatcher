package com.stockwatcher.controller

import com.stockwatcher.model.Quote
import com.stockwatcher.service.StockService
import org.springframework.web.bind.annotation.{GetMapping, PathVariable, RequestMapping, RestController}

@RestController
@RequestMapping(Array("/stock"))
class StockController(stockService: StockService) {

  @GetMapping(value = Array("/quote/{symbol}"))
  def getQuote(@PathVariable("symbol") symbol: String): Quote = {
    stockService.getQuote(symbol)
  }
}
