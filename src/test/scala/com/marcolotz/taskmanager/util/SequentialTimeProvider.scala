package com.marcolotz.taskmanager.util

import com.marcolotz.taskmanager.ports.TimeProvider

import java.time.Instant

object SequentialTimeProvider extends TimeProvider {
  var baseInstant: Instant = Instant.now();

  override def getTime: Instant = {
    baseInstant = baseInstant.plusSeconds(1);
    baseInstant;
  }
}
