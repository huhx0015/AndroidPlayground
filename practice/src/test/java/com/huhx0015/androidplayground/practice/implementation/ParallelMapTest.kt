package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ParallelMapTest {

  @Test
  fun `runs transforms concurrently and preserves order`() = runTest {
    val input = listOf(1, 2, 3, 4, 5)

    val result = input.parallelMap { value ->
      delay(100) // each task takes 100ms
      value * 2
    }

    assertEquals(listOf(2, 4, 6, 8, 10), result)
    // Concurrent execution: ~100ms total, NOT 5 * 100ms = 500ms.
    assertEquals(100, testScheduler.currentTime)
  }

  @Test
  fun `empty input returns empty list`() = runTest {
    val result = emptyList<Int>().parallelMap { it * 2 }
    assertEquals(emptyList<Int>(), result)
  }

  @Test
  fun `fails fast and cancels siblings when a transform throws`() = runTest {
    var completedSlowTask = false

    var caught: Throwable? = null
    try {
      listOf(1, 2, 3).parallelMap { value ->
        if (value == 2) {
          delay(50)
          throw IllegalStateException("transform failed for $value")
        } else {
          delay(500) // slow sibling — should be cancelled before completing
          completedSlowTask = true
          value
        }
      }
    } catch (e: IllegalStateException) {
      caught = e
    }

    assertTrue("expected the transform failure to propagate", caught is IllegalStateException)
    assertEquals("transform failed for 2", caught!!.message)
    // The failure at t=50 must cancel the 500ms siblings before they finish.
    assertTrue("siblings should have been cancelled (fail-fast)", !completedSlowTask)
    assertEquals(50, testScheduler.currentTime)
  }
}
