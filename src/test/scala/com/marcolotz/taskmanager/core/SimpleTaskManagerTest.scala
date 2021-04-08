package com.marcolotz.taskmanager.core

import com.marcolotz.taskmanager.core.datacontainers.ProcessCollector
import com.marcolotz.taskmanager.exception.{MaximumCapacityReachedException, ProcessNotFoundException}
import com.marcolotz.taskmanager.model.{AcceptedProcessDecorator, HIGH_PRIORITY, LOW_PRIORITY, Process}
import com.marcolotz.taskmanager.ports.TaskManager
import com.marcolotz.taskmanager.util.SequentialTimeProvider
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.mockito.MockitoSugar.mock
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.{a, convertToAnyShouldWrapper, equal}

import java.util.UUID
import scala.util.{Failure, Success}

class SimpleTaskManagerTest extends AnyFlatSpec with BeforeAndAfter {

  val processes: ProcessCollector = mock[ProcessCollector]
  var taskManager: TaskManager = _

  before {
    taskManager = new SimpleTaskManager(SequentialTimeProvider, processes)
    reset(processes)
  }

  "Task Manager" should "accept new processes" in {
    // Given
    doNothing().when(processes).addProcess(any())
    // When
    val result = taskManager.addProcess(Process(LOW_PRIORITY))
    // Then
    verify(processes).addProcess(any())
    result shouldBe a[Success[Unit]]
  }

  it should "notify failures when adding to process collection" in {
    // Given
    doThrow(new MaximumCapacityReachedException("boom")).when(processes).addProcess(any())

    // When
    val result = taskManager.addProcess(Process(LOW_PRIORITY))

    // Then
    verify(processes).addProcess(any())
    result shouldBe a[Failure[MaximumCapacityReachedException]]
  }

  it should "list and sort running processes" in {
    // Given
    val lowPrio = AcceptedProcessDecorator(Process(LOW_PRIORITY), SequentialTimeProvider.getTime)
    val highPrio = AcceptedProcessDecorator(Process(HIGH_PRIORITY), SequentialTimeProvider.getTime)

    val mockResponse = List(lowPrio, highPrio)
    doReturn(mockResponse).when(processes).toList
    val expectedResult = mockResponse.sorted(PRIORITY.ordering).map(p => p.process)

    // When
    val result = taskManager.listRunningProcess(PRIORITY)

    // Then
    verify(processes).toList
    result should equal(expectedResult)
  }

  it should "kill all processes" in {
    // Given
    val process = spy(AcceptedProcessDecorator(Process(LOW_PRIORITY), SequentialTimeProvider.getTime))

    val mockResponse = List(process)
    doReturn(mockResponse.toSet).when(processes).removeMatchingProcess(any())

    // When
    taskManager.killAll()

    // Then
    verify(process).kill()
  }

  it should "kill a process specified by the pid" in {
    // Given
    val process = spy(AcceptedProcessDecorator(Process(LOW_PRIORITY), SequentialTimeProvider.getTime))
    val processPid = process.pid

    val mockResponse = List(process)
    doReturn(mockResponse.toSet).when(processes).removeMatchingProcess(any())

    // When
    taskManager.killProcess(processPid.toString)

    // Then
    verify(process).kill()
  }

  it should "notify when a process to kill could not be found" in {
    // Given
    val mockResponse = List()
    doReturn(mockResponse.toSet).when(processes).removeMatchingProcess(any())

    // When
    val result = taskManager.killProcess(UUID.randomUUID().toString)

    // Then
    result shouldBe a[Failure[ProcessNotFoundException]]
  }

  it should "kill process in a specific group" in {
    val lowPrio = spy(AcceptedProcessDecorator(Process(LOW_PRIORITY), SequentialTimeProvider.getTime))

    val mockResponse = List(lowPrio)
    doReturn(mockResponse.toSet).when(processes).removeMatchingProcess(any())

    // When
    taskManager.killGroup(LOW_PRIORITY)

    // Then
    verify(lowPrio).kill()
  }

}
