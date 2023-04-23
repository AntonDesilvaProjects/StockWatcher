package com.stockwatcher.dao.alphavantage

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.stockwatcher.HelperUtils
import com.stockwatcher.model.{Quote, Status}

case class QuoteResponse(@JsonProperty("Global Quote") globalQuote: Quote)

object QuoteResponse extends App {

//  case class Test(@JsonProperty("nombre") name: String)
//
//  val json: String = "{\"nombre\": \"hey\"}";
//  val mapper: ObjectMapper = new ObjectMapper
//  mapper.registerModule(DefaultScalaModule)
//
//  println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(Test("abc")))
//
//
////  val qr = mapper.readValue(json, classOf[QuoteResponse])
////
//  println(mapper.readValue(json, classOf[Test]).name)

//  println(Status(2) == Status.ACTIVE)
//
//  def x(q: Quote): Quote = Option(q).orNull


  println(HelperUtils.round(2.30000000, 2))



}
