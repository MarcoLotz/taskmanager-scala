package com.marcolotz.taskmanager.core.datacontainers

import com.marcolotz.taskmanager.model.AcceptedProcessDecorator
import com.typesafe.scalalogging.LazyLogging

import scala.collection.mutable

class BoundedPriorityQueueProcessCollection(maximumSize: Int)(val ordering: Ordering[AcceptedProcessDecorator])
  extends ProcessCollection(maximumSize: Int)
    with LazyLogging {

  val queue: mutable.PriorityQueue[AcceptedProcessDecorator] = mutable.PriorityQueue()(ordering)

  override def addProcess(process: AcceptedProcessDecorator): Unit = {
    if (queue.size == maximumSize) {
      // replace the minimum priority
      queue.toList
        .find(p => p.priority.priorityNumber < process.priority.priorityNumber)
        .map(p => removeFromQueue(p))
        .getOrElse {
          logger.info("Could not find a process with less priority than {} to remove from the queue", process.priority)
        }
    }
    else {
      queue.enqueue(process)
    }
  }

  def removeFromQueue(removedProcess: AcceptedProcessDecorator): mutable.PriorityQueue[AcceptedProcessDecorator] = {
    val newCollection = queue.toList.filterNot(p => p.equals(removedProcess))
    queue.clear()
    queue.addAll(newCollection)
  }

  override def size: Int = queue.size

  override def toList: List[AcceptedProcessDecorator] = queue.toList

  override def removeMatchingProcess(pred: AcceptedProcessDecorator => Boolean): Set[AcceptedProcessDecorator] = {
    val removedProcesses = queue.filter(pred)
    val list = queue.filterNot(pred)
    queue.clear()
    queue.addAll(list)
    removedProcesses.toSet
  }
}
