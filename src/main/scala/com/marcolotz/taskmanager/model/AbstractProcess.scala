package com.marcolotz.taskmanager.model

import com.typesafe.scalalogging.LazyLogging

import java.time.Instant
import java.util.UUID

sealed trait AbstractProcess {

  def pid: UUID
  def priority: Priority
  def kill(): AbstractProcess
}

// I have decided to assign the PID on construction for this scenario, instead of leaving to the
// constructor to define a PID. Also I decided to use UUID instead of unix like integers.
// The model doesn't specify. This avoids collision on a simple model of the problem.
// If the requirements were different, then a collision avoidance component would have to create pid
// and use as a constructor parameter.
case class Process(priority: Priority) extends AbstractProcess with LazyLogging {
  def pid: UUID = UUID.randomUUID()
  override def kill(): AbstractProcess = {
    logger.info("Process {} just got killed", this)
    // On a different model, the process state could also be changed here.
    this
  }
}

case class AcceptedProcessDecorator(process: com.marcolotz.taskmanager.model.Process, creationTime: Instant) extends AbstractProcess {
  override def kill(): AbstractProcess = process.kill()

  override def pid: UUID = process.pid

  override def priority: Priority = process.priority
}
