package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Verifies the reference solution for EXERCISE 08. Mirrors
 * [com.huhx0015.androidplayground.practice.implementation.RaceFirstSuccessfulTest].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RaceFirstSuccessfulSolutionTest {

  @Test
  fun returnsFirstSuccessfulResult_andCancelsOthers() = runTest {
    var slowBlockCompleted = false

    val result = raceFirstSuccessfulSolution(
      listOf(
        { delay(200); slowBlockCompleted = true; "slow" },
        { delay(50); "fast" },
        { delay(100); "medium" },
      ),
    )

    assertEquals("fast", result)
    advanceUntilIdle()
    assertFalse("slower blocks should have been cancelled", slowBlockCompleted)
  }

  @Test
  fun skipsFailingBlocks_andReturnsFirstSuccess() = runTest {
    val result = raceFirstSuccessfulSolution(
      listOf(
        { delay(10); throw RuntimeException("fails fast") },
        { delay(100); "success" },
      ),
    )

    assertEquals("success", result)
  }

  @Test
  fun whenAllBlocksFail_throws() = runTest {
    var caught: Throwable? = null

    try {
      raceFirstSuccessfulSolution<String>(
        listOf(
          { delay(10); throw RuntimeException("a") },
          { delay(20); throw RuntimeException("b") },
        ),
      )
    } catch (error: RuntimeException) {
      caught = error
    }

    assertNotNull(caught)
  }

  @Test(expected = IllegalArgumentException::class)
  fun emptyList_throwsIllegalArgument() = runTest {
    raceFirstSuccessfulSolution<String>(emptyList())
  }
}
