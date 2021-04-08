package com.marcolotz.taskmanager.core.datacontainers

import com.marcolotz.taskmanager.model.AcceptedProcessDecorator
import org.scalacheck.Prop.forAllNoShrink
import org.scalatest.matchers.must.Matchers.{be, noException, not}
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

object BoundedFifoProcessCollectorTest extends AbstractProcessCollectorTest {

  override protected var maximum_capacity: Int = 100

  override def supplyCollection: ProcessCollector = new BoundedFifoProcessCollector(maximum_capacity)

  property("adding a process should throw exceptions when capacity is reached") =
    forAllNoShrink(processListGenerator) { processes: List[AcceptedProcessDecorator] =>
      val processCollector = supplyCollection
      for ((process, numberOfIngestedProcesses) <- processes.zip(0 to processes.size)) {
        {
          val oldest = processCollector.toList.headOption
          noException should be thrownBy processCollector.addProcess(process)
          if (numberOfIngestedProcesses > maximum_capacity) {
            if (oldest.isDefined) processCollector.toList should not contain oldest
            processCollector.size == maximum_capacity
          }
          processCollector.size <= maximum_capacity
        }
      }
      true
    }
}
