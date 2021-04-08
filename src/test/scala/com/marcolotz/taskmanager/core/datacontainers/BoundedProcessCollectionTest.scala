package com.marcolotz.taskmanager.core.datacontainers

import com.marcolotz.taskmanager.exception.MaximumCapacityReachedException
import com.marcolotz.taskmanager.model.AcceptedProcessDecorator
import org.scalacheck.Prop.{forAll, forAllNoShrink}
import org.scalatest.Assertions.assertThrows
import org.scalatest.matchers.must.Matchers.{be, noException}

import scala.sys.process

object BoundedProcessCollectionTest extends AbstractProcessCollectionTest {
  override def supplyCollection: ProcessCollection = new BoundedProcessCollection(100)

  property("adding a process should throw exceptions when capacity is reached") =
    forAllNoShrink(processListGenerator) { processes: List[AcceptedProcessDecorator] =>
      val collection = supplyCollection
      for ( (process, numberOfIngestedProcesses) <- processes.zip(0 to processes.size)) {
        {
          if (numberOfIngestedProcesses < MAXIMUM_CAPACITY) {
            noException should be thrownBy collection.addProcess(process)
          }
          else {
            assertThrows[MaximumCapacityReachedException] {
              collection.addProcess(process)
            }
          }
        }
      }
      true
    }
}
