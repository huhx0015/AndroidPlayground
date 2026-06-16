package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.io.IOException

/**
 * Verifies the reference solution for EXERCISE 06. Mirrors
 * [com.huhx0015.androidplayground.practice.implementation.WithTimeoutOrFallbackTest].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class WithTimeoutOrFallbackSolutionTest {

  @Test
  fun returnsBlockResultWhenWithinTimeout() = runTest {
    val result = withTimeoutOrFallbackSolution(timeoutMs = 1_000, fallback = "fallback") {
      delay(100)
      "done"
    }

    assertEquals("done", result)
  }

  @Test
  fun returnsFallbackWhenBlockExceedsTimeout() = runTest {
    val result = withTimeoutOrFallbackSolution(timeoutMs = 100, fallback = "fallback") {
      delay(1_000)
      "done"
    }

    assertEquals("fallback", result)
  }

  @Test
  fun cancelsSlowBlockOnTimeout() = runTest {
    var blockCompleted = false

    val result = withTimeoutOrFallbackSolution(timeoutMs = 100, fallback = -1) {
      delay(1_000)
      blockCompleted = true
      42
    }

    assertEquals(-1, result)
    assertFalse("the slow block should have been cancelled", blockCompleted)
  }

  @Test(expected = IOException::class)
  fun realExceptionFromBlockPropagates() = runTest {
    withTimeoutOrFallbackSolution(timeoutMs = 1_000, fallback = "fallback") {
      throw IOException("boom")
    }
  }
}
