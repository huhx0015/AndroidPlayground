package com.huhx0015.androidplayground.practice.debugging.solutions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Verifies the reference fix for D4. Mirrors
 * [com.huhx0015.androidplayground.practice.debugging.BuggyEventBufferTest] and passes.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class EventBufferFixedTest {

  @Test
  fun deliversEveryEventInOrderWhenProducerIsFaster() = runTest {
    val events = flow {
      for (i in 1..5) {
        emit(i)
        delay(10)
      }
    }

    val processed = EventBufferFixed().processEvents(events).toList()

    assertEquals(listOf(1, 2, 3, 4, 5), processed)
  }
}
