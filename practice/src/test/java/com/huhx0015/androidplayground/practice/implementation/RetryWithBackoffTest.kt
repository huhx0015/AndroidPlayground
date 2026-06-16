package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

/**
 * Tests for EXERCISE 02 ([retryWithBackoff]). Remove @Ignore to start.
 *
 * Timing is asserted with runTest's virtual clock: `delay(n)` advances `currentTime` without any
 * real waiting, so backoff growth is fully deterministic.
 */
@Ignore("Remove this annotation to begin Exercise 02")
@OptIn(ExperimentalCoroutinesApi::class)
class RetryWithBackoffTest {

  @Test
  fun succeedsOnFirstAttempt_doesNotDelay() = runTest {
    var attempts = 0

    val result = retryWithBackoff(maxAttempts = 3, initialDelayMs = 100, factor = 2.0) {
      attempts++
      "value"
    }

    assertEquals("value", result)
    assertEquals(1, attempts)
    assertEquals(0L, testScheduler.currentTime)
  }

  @Test
  fun retriesThenSucceeds_withExponentiallyGrowingDelays() = runTest {
    var attempts = 0

    val result = retryWithBackoff(maxAttempts = 4, initialDelayMs = 100, factor = 2.0) {
      attempts++
      if (attempts < 4) error("transient failure $attempts") else "ok"
    }

    assertEquals("ok", result)
    assertEquals(4, attempts)
    // Delays before attempts 2, 3, 4 = 100 + 200 + 400 = 700.
    assertEquals(700L, testScheduler.currentTime)
  }

  @Test
  fun exhaustsAttemptsThenRethrowsLastError() = runTest {
    var attempts = 0
    var caught: Throwable? = null

    try {
      retryWithBackoff(maxAttempts = 3, initialDelayMs = 50, factor = 2.0) {
        attempts++
        throw IllegalStateException("attempt $attempts failed")
      }
    } catch (error: IllegalStateException) {
      caught = error
    }

    assertEquals(3, attempts)
    assertEquals("attempt 3 failed", caught?.message)
  }
}
