package com.marcolotz.taskmanager.ports

import java.time.Instant

trait TimeProvider {

  def getTime: Instant

}
