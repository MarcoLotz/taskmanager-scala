package com.marcolotz.taskmanager.core.datacontainers

import com.marcolotz.taskmanager.model._
import com.marcolotz.taskmanager.util.SequentialTimeProvider
import org.scalacheck.Gen
import org.scalacheck.Prop.forAllNoShrink
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers.contain
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.scalacheck.{Checkers, ScalaCheckDrivenPropertyChecks}

abstract class AbstractProcessCollectorTest extends AnyFlatSpec with ScalaCheckDrivenPropertyChecks with Checkers {

  val processGenerator: Gen[AcceptedProcessDecorator] = for {
    priority <- Gen.oneOf(LOW_PRIORITY, MEDIUM_PRIORITY, HIGH_PRIORITY)
  } yield AcceptedProcessDecorator(Process(priority), SequentialTimeProvider.getTime)
  val processListGenerator: Gen[List[AcceptedProcessDecorator]] = Gen.nonEmptyListOf(processGenerator) suchThat (_.nonEmpty)
  protected val MAXIMUM_NUMBER_OF_GENERATED_PROCESSES: Int = 500
  protected var maximum_capacity: Int

  def supplyCollection: ProcessCollector

  "toList" should "always returns the list equivalent of the internal collection" in {
    forAllNoShrink(processListGenerator) { processes: List[AcceptedProcessDecorator] =>
      val collection = supplyCollection
      processes.foreach(p => collection.addProcess(p))

      val listEquivalent = collection.toList
      listEquivalent should contain allElementsOf processes
      listEquivalent.size == processes.size
    }
  }

  "size" should "always retun the size of the internal number of running processes" in {
    forAllNoShrink(processListGenerator) { processes: List[AcceptedProcessDecorator] =>
      val collection = supplyCollection
      processes.foreach(p => collection.addProcess(p))
      collection.size == processes.size
    }
  }


  "removeMatchingProcess" should "always remove all processes that the predicate is true" in {
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
      collection.toList should contain allElementsOf expectedCollectionState
      removed should contain allElementsOf (expectedRemovals)
      removed.size == expectedRemovals.size
    }
  }

}
