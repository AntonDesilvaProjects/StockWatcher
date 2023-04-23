package com.stockwatcher

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class ApplicationConfig {

  @Bean
  def objectMapper(): ObjectMapper = {
    val objectMapper = new ObjectMapper()
    objectMapper.registerModule(DefaultScalaModule)
    objectMapper
  }

  @Bean
  def restTemplate(): RestTemplate = {
    val restTemplate = new RestTemplate()
    restTemplate.getMessageConverters.stream()
      .filter(_.getClass == classOf[MappingJackson2HttpMessageConverter])
      .map(_.asInstanceOf[MappingJackson2HttpMessageConverter])
      .findFirst
      .ifPresent(converter => converter.getObjectMapper.registerModule(DefaultScalaModule))
    restTemplate
  }
}
