package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FirstSuccessfulTest {

  @Test
  fun `returns the result of the fastest successful task`() = runTest {
    val tasks: List<suspend () -> String> = listOf(
      { delay(300); "slow" },
      { delay(100); "fast" },
      { delay(200); "medium" },
    )

    val result = firstSuccessful(tasks)

    assertEquals("fast", result)
    // Winner resolves at t=100; losers are cancelled.
    assertEquals(100, testScheduler.currentTime)
  }

  @Test
  fun `ignores failures and returns the first task that succeeds`() = runTest {
    val tasks: List<suspend () -> String> = listOf(
      { delay(50); throw RuntimeException("task 0 failed") },
      { delay(150); "winner" },
      { delay(100); throw RuntimeException("task 2 failed") },
    )

    val result = firstSuccessful(tasks)

    assertEquals("winner", result)
    assertEquals(150, testScheduler.currentTime)
  }

  @Test
  fun `throws when every task fails`() = runTest {
    val tasks: List<suspend () -> String> = listOf(
      { delay(50); throw IllegalStateException("nope 0") },
      { delay(100); throw IllegalStateException("nope 1") },
    )

    var caught: Throwable? = null
    try {
      firstSuccessful(tasks)
    } catch (e: IllegalStateException) {
      caught = e
    }

    assertTrue("expected the task failure to surface when all tasks fail", caught is IllegalStateException)
  }
}
