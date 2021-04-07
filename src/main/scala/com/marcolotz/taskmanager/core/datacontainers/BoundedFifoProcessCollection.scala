package com.marcolotz.taskmanager.core.datacontainers

import com.marcolotz.taskmanager.model.AcceptedProcessDecorator

import scala.collection.immutable.Queue

class BoundedFifoProcessCollection(maximumSize: Int) extends ProcessCollection(maximumSize) {

  private var queue: Queue[AcceptedProcessDecorator] = Queue()

  override def addProcess(process: AcceptedProcessDecorator): Unit = {
    if (queue.size.equals(maximumSize)) {
      {
        val (processToRemove, newQueue) = queue.dequeue
        processToRemove.kill()
        queue = newQueue
      }
      queue :: List(process)
    }
  }

  override def size: Int = queue.size

  override def toList: List[AcceptedProcessDecorator] = queue.toList

  override def removeMatchingProcess(pred: AcceptedProcessDecorator => Boolean): Set[AcceptedProcessDecorator] = {
    val removedProcesses = queue.filter(pred)
    queue = queue.filterNot(pred)
    removedProcesses.toSet
  }
}
