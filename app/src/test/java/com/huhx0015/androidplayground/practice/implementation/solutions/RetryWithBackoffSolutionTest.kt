package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Verifies the reference solution for EXERCISE 02. Mirrors
 * [com.huhx0015.androidplayground.practice.implementation.RetryWithBackoffTest].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RetryWithBackoffSolutionTest {

  @Test
  fun succeedsOnFirstAttempt_doesNotDelay() = runTest {
    var attempts = 0

    val result = retryWithBackoffSolution(maxAttempts = 3, initialDelayMs = 100, factor = 2.0) {
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

    val result = retryWithBackoffSolution(maxAttempts = 4, initialDelayMs = 100, factor = 2.0) {
      attempts++
      if (attempts < 4) error("transient failure $attempts") else "ok"
    }

    assertEquals("ok", result)
    assertEquals(4, attempts)
    assertEquals(700L, testScheduler.currentTime)
  }

  @Test
  fun exhaustsAttemptsThenRethrowsLastError() = runTest {
    var attempts = 0
    var caught: Throwable? = null

    try {
      retryWithBackoffSolution(maxAttempts = 3, initialDelayMs = 50, factor = 2.0) {
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
