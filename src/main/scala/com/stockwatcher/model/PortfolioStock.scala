package com.stockwatcher.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

@JsonInclude(Include.NON_NULL)
case class PortfolioStock(portfolioId: Int,
                          comment: String,
                          symbol: String,
                          quantity: Int,
                          value: Double,
                          change: Double,
                          quote: Quote,
                          createdOn: Long,
                          updatedOn: Long)
