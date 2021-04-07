package com.marcolotz.taskmanager.core.datacontainers

import com.marcolotz.taskmanager.exception.MaximumCapacityReachedException
import com.marcolotz.taskmanager.model.AcceptedProcessDecorator

class BoundedProcessCollection(maximumSize: Int) extends ProcessCollection(maximumSize: Int) {

  private var list: List[AcceptedProcessDecorator] = List()

  override def addProcess(process: AcceptedProcessDecorator): Unit = {
    if (list.size + 1 > maximumSize) throw new MaximumCapacityReachedException("Task Manager at max design capacity of " + maximumSize)
    list = list ::: List(process)
  }

  override def size: Int = list.size

  override def toList: List[AcceptedProcessDecorator] = list

  override def removeMatchingProcess(pred: AcceptedProcessDecorator => Boolean): Set[AcceptedProcessDecorator] = {
    val removedProcesses = list.filter(pred)
    list = list.filterNot(pred)
    removedProcesses.toSet
  }
}
