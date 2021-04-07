package com.marcolotz.taskmanager.ports

import com.marcolotz.taskmanager.core.SortingMethod
import com.marcolotz.taskmanager.exception.ProcessNotFoundException
import com.marcolotz.taskmanager.model.{Priority, Process}

import scala.util.Try

trait TaskManager {

  def addProcess(process: Process): Try[Unit]

  def listRunningProcess(method: SortingMethod): Seq[Process]

  @throws(classOf[ProcessNotFoundException])
  def killProcess(pid: String): Try[Unit]

  def killGroup(priority: Priority): Unit

  def killAll(): Unit
}
