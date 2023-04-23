package com.stockwatcher.model

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.{JsonAlias, JsonInclude, JsonProperty}

@JsonInclude(Include.NON_NULL)
case class Quote(@JsonAlias(Array("01. symbol")) @JsonProperty("symbol") symbol: String,
                 @JsonAlias(Array("02. open")) @JsonProperty("open") open: Float,
                 @JsonAlias(Array("03. high")) @JsonProperty("high") high: Float,
                 @JsonAlias(Array("04. low")) @JsonProperty("low") low: Float,
                 @JsonAlias(Array("05. price")) @JsonProperty("price") price: Float,
                 @JsonAlias(Array("06. volume")) @JsonProperty("volume") volume: Int,
                 @JsonAlias(Array("07. latest trading day")) @JsonProperty("latestTradingDay") latestTradingDay: String,
                 @JsonAlias(Array("08. previous close")) @JsonProperty("previousClose") previousClose: Float,
                 @JsonAlias(Array("09. change")) @JsonProperty("change") change: Float,
                 @JsonAlias(Array("10. change percent")) @JsonProperty("changePercent") changePercent: String)
