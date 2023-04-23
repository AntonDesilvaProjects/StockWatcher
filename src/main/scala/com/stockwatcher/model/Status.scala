package com.stockwatcher.model

import com.fasterxml.jackson.core.`type`.TypeReference

object Status extends Enumeration {
  type Status = Value
  val ACTIVE = Value(1, "Active")
  val DELETED = Value(2, "Deleted")
}

class StatusType extends TypeReference[Status.type]
