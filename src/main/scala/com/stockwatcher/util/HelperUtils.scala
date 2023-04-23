package com.stockwatcher

import java.sql.Timestamp

object HelperUtils {
  def toEpochMilli(timestamp: Timestamp): Option[Long] = Option(timestamp).map(_.toInstant).map(_.toEpochMilli)
  def round(amount: Double, scale: Int): Double = BigDecimal(amount)
    .setScale(scale, BigDecimal.RoundingMode.HALF_UP).toDouble
}
