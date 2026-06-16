package com.huhx0015.androidplayground.practice.debugging

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * ============================================================================
 *  DO NOT MODIFY THIS TEST. Fix the bug in EventBuffer so that it passes.
 * ============================================================================
 */
class EventBufferTest {

  @Test
  fun `delivers every event in order even when the producer is faster`() = runTest {
    // Producer emits one event every 10ms; the processor takes 100ms per event.
    val events = flow {
      for (i in 1..5) {
        emit(i)
        delay(10)
      }
    }

    val processed = processEvents(events).toList()

    assertEquals(listOf(1, 2, 3, 4, 5), processed)
  }
}
