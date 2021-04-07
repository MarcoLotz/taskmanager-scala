package com.marcolotz.taskmanager.core

import com.marcolotz.taskmanager.core.datacontainers.ProcessCollection
import com.marcolotz.taskmanager.exception.ProcessNotFoundException
import com.marcolotz.taskmanager.model.{AcceptedProcessDecorator, Priority, Process}
import com.marcolotz.taskmanager.ports.{TaskManager, TimeProvider}

import java.util.UUID
import scala.util.{Failure, Success, Try}

class SimpleTaskManager(val timeProvider: TimeProvider, val tasks: ProcessCollection) extends TaskManager {

  override def addProcess(process: Process): Try[Unit] =
    Try {
      tasks.addProcess(AcceptedProcessDecorator(process, timeProvider.getTime))
    }

  override def listRunningProcess(method: SortingMethod): Seq[Process] = tasks.toList.sorted(method.ordering).map(p => p.process)

  // Note: nowhere is enforced that process should be unique by PID. Probably this should be on ProcessCollectionLevel
  override def killProcess(pid: String): Try[Unit] = {
    val uuid = UUID.fromString(pid)

    Option(tasks.removeMatchingProcess(p => p.pid.equals(uuid)))
      .filter(collection => collection.size == 1)
      .map(collection => collection.head)
      .map(p => p.kill())
      .map(_ => Success())
      .getOrElse(Failure(new ProcessNotFoundException("could not find the process with pid: " + pid)))
  }

  override def killGroup(priority: Priority): Unit =
    tasks
      .removeMatchingProcess(p => p.priority.equals(priority))
      .map(p => p.kill())

  override def killAll(): Unit = tasks.removeMatchingProcess(_ => true).foreach(p => p.kill())
}
