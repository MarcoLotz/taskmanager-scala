package com.marcolotz.taskmanager.core.datacontainers

import com.marcolotz.taskmanager.core.PRIORITY
import com.marcolotz.taskmanager.model.AcceptedProcessDecorator
import org.scalacheck.Prop.forAllNoShrink
import org.scalatest.matchers.must.Matchers.{be, contain, noException, not}
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

object BoundedPriorityQueueProcessCollectorTest extends AbstractProcessCollectorTest {

  override protected var maximum_capacity: Int = 100

  override def supplyCollection: ProcessCollector = new BoundedPriorityQueueProcessCollector(maximum_capacity)(PRIORITY.ordering)

  property("adding a process should remove processes with lower priority when the collection is full") =
    forAllNoShrink(processListGenerator) { processes: List[AcceptedProcessDecorator] =>
      val processCollector = supplyCollection
      for ((process, numberOfIngestedProcesses) <- processes.zip(0 to processes.size)) {
        {
          val originalLowPriorityProcesses = retrieveLowestPriority(processCollector.toList)
          noException should be thrownBy processCollector.addProcess(process)
          if (numberOfIngestedProcesses > maximum_capacity) {

            if (originalLowPriorityProcesses.headOption.exists(p => p.priority.priorityNumber < process.priority.priorityNumber)) {
              // Should be a replace lowest priority
              val updatedLowPriority = retrieveLowestPriority(processCollector.toList)
              processCollector.toList should contain(process)
              originalLowPriorityProcesses.toList should contain allElementsOf (updatedLowPriority)
              updatedLowPriority.size shouldBe originalLowPriorityProcesses.size - 1
            }
            else // Recently added process should be ignored
            {
              processCollector.toList should not contain process
            }
            processCollector.size == maximum_capacity
          }
          processCollector.size <= maximum_capacity
        }
      }
      true
    }

  def retrieveLowestPriority(pCollection: List[AcceptedProcessDecorator]): Set[AcceptedProcessDecorator] =
    pCollection.sorted(PRIORITY.ordering).reverse.headOption
      .map(_.priority)
      .map(priority => pCollection.filter(p => p.priority == priority))
      .map(_.toSet)
      .getOrElse(Set())

}
