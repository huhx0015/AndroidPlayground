package com.huhx0015.androidplayground.practice.debugging

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

/**
 * DEBUGGING EXERCISE D4 test. Remove @Ignore to reproduce the failure, then fix [BuggyEventBuffer].
 */
@Ignore("Remove this annotation to work DEBUGGING EXERCISE D4")
@OptIn(ExperimentalCoroutinesApi::class)
class BuggyEventBufferTest {

  @Test
  fun deliversEveryEventInOrderWhenProducerIsFaster() = runTest {
    // Producer emits one event every 10ms; the processor takes 100ms per event.
    val events = flow {
      for (i in 1..5) {
        emit(i)
        delay(10)
      }
    }

    val processed = BuggyEventBuffer().processEvents(events).toList()

    assertEquals(listOf(1, 2, 3, 4, 5), processed)
  }
}
