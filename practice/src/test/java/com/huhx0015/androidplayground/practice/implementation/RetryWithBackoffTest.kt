package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RetryWithBackoffTest {

  @Test
  fun `retries until success and applies exponential backoff`() = runTest {
    var attempts = 0
    val upstream = flow {
      attempts++
      if (attempts < 3) throw RuntimeException("boom #$attempts")
      emit("ok")
    }

    val value = upstream
      .retryWithBackoff(maxAttempts = 3, initialDelayMillis = 100, factor = 2.0)
      .first()

    assertEquals("ok", value)
    assertEquals(3, attempts)
    // Backoff before retry 0 = 100ms, before retry 1 = 200ms -> 300ms of virtual time.
    assertEquals(300, testScheduler.currentTime)
  }

  @Test
  fun `rethrows the last error after attempts are exhausted`() = runTest {
    var attempts = 0
    val upstream = flow<String> {
      attempts++
      throw IllegalStateException("always fails #$attempts")
    }

    var caught: Throwable? = null
    try {
      upstream
        .retryWithBackoff(maxAttempts = 2, initialDelayMillis = 100, factor = 2.0)
        .first()
    } catch (e: IllegalStateException) {
      caught = e
    }

    assertEquals(2, attempts) // 1 initial attempt + 1 retry
    assertTrue("expected the last failure to propagate", caught is IllegalStateException)
    assertTrue(caught!!.message!!.startsWith("always fails"))
    // One backoff of 100ms happened before the single retry.
    assertEquals(100, testScheduler.currentTime)
  }
}
