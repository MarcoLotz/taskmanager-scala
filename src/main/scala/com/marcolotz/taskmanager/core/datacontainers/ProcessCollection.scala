package com.marcolotz.taskmanager.core.datacontainers

import com.marcolotz.taskmanager.exception.MaximumCapacityReachedException
import com.marcolotz.taskmanager.model.AcceptedProcessDecorator

abstract class ProcessCollection(val maximumSize: Int) {
  // constructor validation
  if (maximumSize < 1) throw new IllegalArgumentException("Capacity needs to be higher than 0")

  @throws(classOf[MaximumCapacityReachedException]) //(https://bit.ly/3sTcrzU)
  def addProcess(process: AcceptedProcessDecorator): Unit

  def size: Int

  // For O(n) operations in the collection
  def toList: List[AcceptedProcessDecorator]

  def removeMatchingProcess(pred: AcceptedProcessDecorator => Boolean): Set[AcceptedProcessDecorator]

}
