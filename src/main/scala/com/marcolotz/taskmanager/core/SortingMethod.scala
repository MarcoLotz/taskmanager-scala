package com.marcolotz.taskmanager.core

import com.marcolotz.taskmanager.model.AcceptedProcessDecorator

import java.time.Instant
import java.util.UUID

// So much better than java if else evaluation
sealed trait SortingMethod {
  def ordering: Ordering[AcceptedProcessDecorator] = {
    this match {
      case CREATION_TIME => Ordering.by[AcceptedProcessDecorator, Instant](_.creationTime).reverse
      case PRIORITY => Ordering.by[AcceptedProcessDecorator, Int](_.priority.priorityNumber).reverse
      case _ => Ordering.by[AcceptedProcessDecorator, UUID](_.pid).reverse
    }
  }
}

case object CREATION_TIME extends SortingMethod

case object PRIORITY extends SortingMethod

case object ID extends SortingMethod
