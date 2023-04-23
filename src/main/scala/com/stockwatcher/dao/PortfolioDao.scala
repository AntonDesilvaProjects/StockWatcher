package com.stockwatcher.dao

import com.stockwatcher.model.Portfolio

trait PortfolioDao {

  def get(id: Int): Option[Portfolio]

  def create(portfolio: Portfolio): Portfolio

}
