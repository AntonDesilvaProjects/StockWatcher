package com.stockwatcher.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.module.scala.JsonScalaEnumeration
import com.stockwatcher.model.Status.Status

@JsonInclude(Include.NON_NULL)
case class Portfolio(id: Int,
                     name: String,
                     description: String,
                     stocks: List[PortfolioStock],
                     change: Double,
                     value: Double = 0,
                     createdOn: Long,
                     updatedOn: Long,
                     @JsonScalaEnumeration(classOf[StatusType]) status: Status) extends Serializable

