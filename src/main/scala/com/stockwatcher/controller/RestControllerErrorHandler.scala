package com.stockwatcher.controller

import com.stockwatcher.model.ApiError
import com.typesafe.scalalogging.Logger
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.{ControllerAdvice, ExceptionHandler}

import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class RestControllerErrorHandler {

  private val logger = Logger(classOf[RestControllerErrorHandler])

  @ExceptionHandler(Array(classOf[Exception]))
  def handleError(request: HttpServletRequest, exception: Exception): ResponseEntity[ApiError] = {
    val url = request.getRequestURL.toString
    val method = request.getMethod
    logger.error(s"Error while handling [$method] $url", exception)
    val error = buildError(exception, url, method)
    new ResponseEntity[ApiError](error, error.httpStatus)
  }

  private def buildError(exception: Exception, url: String, method: String): ApiError = {
    exception match {
      case i: IllegalArgumentException => ApiError(HttpStatus.BAD_REQUEST, method, url, i.getMessage)
      case _ => ApiError(HttpStatus.INTERNAL_SERVER_ERROR, method, url, exception.getMessage)
    }
  }
}
