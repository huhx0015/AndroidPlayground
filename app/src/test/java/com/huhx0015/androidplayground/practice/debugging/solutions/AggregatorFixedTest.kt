package com.huhx0015.androidplayground.practice.debugging.solutions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Verifies the reference fix for D2. Mirrors
 * [com.huhx0015.androidplayground.practice.debugging.BuggyAggregatorTest] and passes.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AggregatorFixedTest {

  @Test
  fun sumsAllTaskResults() = runTest {
    val total = AggregatorFixed().sumAll(
      listOf(
        { delay(10); 1 },
        { delay(20); 2 },
        { delay(30); 3 },
      ),
    )

    assertEquals(6, total)
  }

  @Test
  fun emptyTaskList_returnsZero() = runTest {
    val total = AggregatorFixed().sumAll(emptyList())

    assertEquals(0, total)
  }
}
