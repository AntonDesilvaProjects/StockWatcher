package com.stockwatcher.dao.alphavantage

import com.stockwatcher.dao.alphavantage.AlphaVantageStockApi.{API_KEY, FUNCTION}
import com.stockwatcher.model.Quote
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.{CollectionUtils, LinkedMultiValueMap, MultiValueMap, MultiValueMapAdapter}
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class AlphaVantageStockApi(restTemplate: RestTemplate,
                           @Value("${stock.client.alphavantage.host:''}") host: String,
                           @Value("${stock.client.alphavantage.apiKey:''}") apiKey: String) {

  private val QUERY_URL = s"$host/query"
  private val FUNCTION_QUOTE = "GLOBAL_QUOTE"

  def getQuote(symbol: String): Quote = {
    val response: ResponseEntity[QuoteResponse] = restTemplate.getForEntity(buildUrl(FUNCTION_QUOTE,
      Map("symbol" -> symbol)), classOf[QuoteResponse])
    Option(response.getBody)
      .map(_.globalQuote)
      .filter(quote => StringUtils.isNotEmpty(quote.symbol))
      .getOrElse(throw new RuntimeException(s"AlphaVantage API did not return a valid response for symbol=[$symbol]"))
  }

  private def buildUrl(function: String, additionalParams: Map[String, String]) = {
    val uriBuilder: UriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(QUERY_URL)
      .queryParam(API_KEY, apiKey)
      .queryParam(FUNCTION, function)

    // convert map to MultiValueMap
    val queryParamsMap = new LinkedMultiValueMap[String, String]()
    additionalParams.foreach(kv => queryParamsMap.add(kv._1, kv._2))
    uriBuilder.queryParams(queryParamsMap)

    uriBuilder.build().toUri
  }
}

object AlphaVantageStockApi /*extends App*/ {

  private val FUNCTION = "function"
  private val API_KEY = "apikey"

//  println(builder("GLOBAL_QUOTE", Map("symbol" -> "ADBE")).toURL)
//
//  def builder(function: String, additionalParams: Map[String, String]) = {
//    val uriBuilder: UriComponentsBuilder = UriComponentsBuilder.fromHttpUrl("https://www.alphavantage.co/query")
//      .queryParam("apiKey", "my_key")
//      .queryParam("function", function)
//
//    // convert map to multivaluemap
//    val queryParamsMap = new LinkedMultiValueMap[String, String]()
//    additionalParams.foreach(kv => queryParamsMap.add(kv._1, kv._2))
//    uriBuilder.queryParams(queryParamsMap)
//
//    uriBuilder.build().toUri
//  }


}

