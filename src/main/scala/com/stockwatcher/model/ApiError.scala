package com.stockwatcher.model

import org.springframework.http.HttpStatus

import java.time.Instant

case class ApiError(httpStatus: HttpStatus,
                    method: String,
                    url: String,
                    error: String,
                    timestamp: Long = Instant.now().toEpochMilli)
