package com.stockwatcher.service

import com.stockwatcher.model.Portfolio

trait PortfolioService {

  /**
   * Gets a portfolio using id. Additional generates aggregate metrics such as total portfolio value, change,
   * etc using current stock prices.
   *
   * @param id of the portfolio
   * @return Portfolio
   */
  def get(id: Int): Portfolio

  /**
   * Validates, creates and persists a portfolio.
   *
   * @param portfolio portfolio to create
   * @return Portfolio object with id
   */
  def create(portfolio: Portfolio): Portfolio

  /**
   * Updates portfolio.
   *
   * @param portfolio
   * @return updated Portfolio
   */
  def update(portfolio: Portfolio): Portfolio

  /**
   * Deletes the portfolio with specified id
   *
   * @param id of the Portfolio to delete
   */
  def delete(id: Int): Unit
}
