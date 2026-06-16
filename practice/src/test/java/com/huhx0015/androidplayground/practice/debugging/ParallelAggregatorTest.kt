package com.huhx0015.androidplayground.practice.debugging

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * ============================================================================
 *  DO NOT MODIFY THIS TEST. Fix the bug in ParallelAggregator so that it passes.
 * ============================================================================
 */
class ParallelAggregatorTest {

  @Test
  fun `sums results of all tasks when they all succeed`() = runTest {
    val tasks: List<suspend () -> Int> = listOf(
      { delay(10); 1 },
      { delay(10); 2 },
      { delay(10); 3 },
    )

    assertEquals(6, aggregate(tasks))
  }

  @Test
  fun `propagates the exception when any task fails`() = runTest {
    val tasks: List<suspend () -> Int> = listOf(
      { delay(10); 1 },
      { delay(10); throw IllegalStateException("task 1 failed") },
      { delay(10); 3 },
    )

    var caught: Throwable? = null
    try {
      aggregate(tasks)
    } catch (e: IllegalStateException) {
      caught = e
    }

    assertTrue("aggregate must fail fast, not return a partial sum", caught is IllegalStateException)
    assertEquals("task 1 failed", caught!!.message)
  }
}
