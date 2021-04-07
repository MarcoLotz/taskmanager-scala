package com.marcolotz.taskmanager.core.datacontainers

import com.marcolotz.taskmanager.model.AcceptedProcessDecorator

class BoundedFifoProcessCollection(maximumSize: Int) extends ProcessCollection(maximumSize) {

  private var list: List[AcceptedProcessDecorator] = List()

  override def addProcess(process: AcceptedProcessDecorator): Unit = {
    if (list.size.equals(maximumSize)) {
      {
        val removedProcess = list.head
        list = list.drop(0)
        removedProcess.kill()
      }
      list :: List(process)
    }
  }

  override def size: Int = list.size

  override def toList: List[AcceptedProcessDecorator] = list

  override def removeMatchingProcess(pred: AcceptedProcessDecorator => Boolean): Set[AcceptedProcessDecorator] = {
    val removedProcesses = list.filter(pred)
    list = list.filterNot(pred)
    removedProcesses.toSet
  }
}
