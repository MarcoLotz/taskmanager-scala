package com.marcolotz.taskmanager.core

import com.marcolotz.taskmanager.ports.TimeProvider

import java.time.Instant

object SystemTimeProvider extends TimeProvider {
  override def getTime: Instant = Instant.now()
}
