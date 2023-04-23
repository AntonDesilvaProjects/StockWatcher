package com.stockwatcher.controller

import com.stockwatcher.model.Portfolio
import com.stockwatcher.service.PortfolioService
import org.springframework.web.bind.annotation.{GetMapping, PathVariable, PostMapping, RequestBody, RequestMapping, RestController}

@RestController
@RequestMapping(Array("/portfolio"))
class PortfolioController(portfolioService: PortfolioService) {

  @GetMapping(Array("{id}"))
  def get(@PathVariable("id") id: Int): Portfolio = {
    portfolioService.get(id)
  }

  @PostMapping
  def create(@RequestBody portfolio: Portfolio): Portfolio = {
    portfolioService.create(portfolio)
  }
}
