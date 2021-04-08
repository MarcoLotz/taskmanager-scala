package com.marcolotz.taskmanager.core.datacontainers

import com.marcolotz.taskmanager.model._
import com.marcolotz.taskmanager.util.SequentialTimeProvider
import org.scalacheck.Prop.forAllNoShrink
import org.scalacheck.{Gen, Properties}
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

abstract class AbstractProcessCollectorTest extends Properties("ProcessCollection") {

  protected val MAXIMUM_NUMBER_OF_GENERATED_PROCESSES: Int = 500
  protected var maximum_capacity: Int

  val processGenerator: Gen[AcceptedProcessDecorator] = for {
    priority <- Gen.oneOf(LOW_PRIORITY, MEDIUM_PRIORITY, HIGH_PRIORITY)
  } yield AcceptedProcessDecorator(Process(priority), SequentialTimeProvider.getTime)

  val processListGenerator: Gen[List[AcceptedProcessDecorator]] = Gen.nonEmptyListOf(processGenerator) suchThat (_.nonEmpty)

  def supplyCollection: ProcessCollector

  property("toList should always return the list equivalent of the internal collection") =
    forAllNoShrink(processListGenerator) { processes: List[AcceptedProcessDecorator] =>
      val collection = supplyCollection
      processes.foreach(p => collection.addProcess(p))

      val listEquivalent = collection.toList
      listEquivalent.equals(processes)
    }

  property("size should always return the size of the internal number of running processes") =
    forAllNoShrink(processListGenerator) { processes: List[AcceptedProcessDecorator] =>
      val collection = supplyCollection
      processes.foreach(p => collection.addProcess(p))
      collection.size == processes.size
    }


  property("removeMatchingProcess should always remove all processes that the predicate is true") =
    forAllNoShrink(processListGenerator) { processes: List[AcceptedProcessDecorator] =>
      // Given
      val collection = supplyCollection
      processes.foreach(p => collection.addProcess(p))
      val predicate: AcceptedProcessDecorator => Boolean = p => p.priority.equals(HIGH_PRIORITY)
      val expectedRemovals = processes.filter(predicate).toSet
      val expectedCollectionState = processes.filterNot(predicate)

      // When
      val removed = collection.removeMatchingProcess(predicate)

      // Then
      collection.size shouldBe (expectedCollectionState.size)
      collection.toList shouldBe expectedCollectionState
      removed.equals(expectedRemovals)
    }

}
