# StockWatcher

## APIs

## Project Setup

Steps to set up a Gradle-based Spring Boot Scala Microservice project:

1. Create an empty IntelliJ project
2. Add a `build.gradle` file with the following contents:

```
plugins {
    id 'java'
    id 'scala'
    id 'org.springframework.boot' version '2.7.10'
    id 'io.spring.dependency-management' version '1.0.15.RELEASE'
}

group 'org.example'
version '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.scala-lang:scala-library:2.13.9'
    implementation 'com.fasterxml.jackson.module:jackson-module-scala_2.13:2.13.5'

    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.1'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.8.1'
}

test {
    useJUnitPlatform()
}
```

This build file has the Spring boot dependencies along with Scala library and Jackson module.

3. Add new directories to the project by right-clicking on project name and clicking `New` > `Directory` and we can 
select the standard "source sets" for source code, tests, resources, etc. 

4. Set up the following two files:

Application Runner:
```scala
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Application

object Application extends App {
  SpringApplication.run(classOf[Application])
}
```

Config class to enable proper Jackson de-/serialization
```scala
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.springframework.context.annotation.{Bean, Configuration}

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
```

5. Set up a REST controller and run app!

```scala
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
```