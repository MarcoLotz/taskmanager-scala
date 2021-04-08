package com.marcolotz.taskmanager.core

import com.marcolotz.taskmanager.model._
import com.marcolotz.taskmanager.ports.TimeProvider
import com.marcolotz.taskmanager.util.SequentialTimeProvider
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.{convertToAnyShouldWrapper, equal}

class SortingMethodTest extends AnyFlatSpec with BeforeAndAfter {

  val timeProvider: TimeProvider = SequentialTimeProvider

  private val low = Process(LOW_PRIORITY)
  private val medium = Process(MEDIUM_PRIORITY)
  private val high = Process(HIGH_PRIORITY)

  var inputList: List[AcceptedProcessDecorator] = List()

  before {
    inputList = List(
      AcceptedProcessDecorator(low, timeProvider.getTime),
      AcceptedProcessDecorator(medium, timeProvider.getTime),
      AcceptedProcessDecorator(high, timeProvider.getTime))
  }

  // TODO: Fix flaky test
  "Sorting processes by id" should "return a descending list of id numbers" ignore {
    val sortedList = inputList.sorted(ID.ordering)
    sortedList.map(p => p.process) should equal(List(high, medium, low).sortBy(p => p.pid).reverse)
  }

  "Sorting processes by creation date" should "return a list with the latest first" in {
    val sortedList = inputList.sorted(CREATION_TIME.ordering)
    sortedList.map(p => p.process) should equal(List(high, medium, low))
  }

  "Sorting processes by priority" should "return a list the highest priority first" in {
    val sortedList = inputList.sorted(PRIORITY.ordering)
    sortedList.map(p => p.process) should equal(List(high, medium, low))
  }

}
