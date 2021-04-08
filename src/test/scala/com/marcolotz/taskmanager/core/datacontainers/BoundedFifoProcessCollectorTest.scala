package com.marcolotz.taskmanager.core.datacontainers

import com.marcolotz.taskmanager.model.AcceptedProcessDecorator
import org.scalacheck.Prop.forAllNoShrink
import org.scalatest.matchers.must.Matchers.{be, noException}

object BoundedFifoProcessCollectorTest extends AbstractProcessCollectorTest {

  // TODO:
  val maximum_capacity = 100

  override def supplyCollection: ProcessCollector = new BoundedFifoProcessCollector(maximum_capacity)

  property("adding a process should throw exceptions when capacity is reached") =
    forAllNoShrink(processListGenerator) { processes: List[AcceptedProcessDecorator] =>
      val processCollector = supplyCollection
      for ((process, numberOfIngestedProcesses) <- processes.zip(0 to processes.size)) {
        {
          // TODO: Track removal
          //val oldest = processCollector.toList.head
          noException should be thrownBy processCollector.addProcess(process)
          if (numberOfIngestedProcesses > maximum_capacity) {
            // TODO: Use scala test syntax here
            //assert(!processCollector.toList.contains(oldest))
            processCollector.size == maximum_capacity
          }
          processCollector.size <= maximum_capacity
        }
      }
      true
    }

}
