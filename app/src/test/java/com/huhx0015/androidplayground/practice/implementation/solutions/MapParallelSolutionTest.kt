package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max

/**
 * Verifies the reference solution for EXERCISE 01. These run by default and prove the answer is
 * correct. They mirror [com.huhx0015.androidplayground.practice.implementation.MapParallelTest].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MapParallelSolutionTest {

  @Test
  fun preservesOrder_evenWhenLaterItemsFinishFirst() = runTest {
    val input = listOf(1, 2, 3, 4, 5)

    val result = input.mapParallelSolution(concurrency = 2) { value ->
      delay((6 - value) * 10L)
      value * 2
    }

    assertEquals(listOf(2, 4, 6, 8, 10), result)
  }

  @Test
  fun neverExceedsConcurrencyLimit() = runTest {
    val concurrency = 3
    val inFlight = AtomicInteger(0)
    val maxObserved = AtomicInteger(0)

    (1..20).toList().mapParallelSolution(concurrency = concurrency) { value ->
      val current = inFlight.incrementAndGet()
      maxObserved.updateAndGet { observed -> max(observed, current) }
      delay(50)
      inFlight.decrementAndGet()
      value
    }

    assertTrue(
      "max in-flight ${maxObserved.get()} should never exceed limit $concurrency",
      maxObserved.get() <= concurrency,
    )
  }

  @Test
  fun emptyInput_returnsEmptyList() = runTest {
    val result = emptyList<Int>().mapParallelSolution(concurrency = 4) { it * 2 }

    assertTrue(result.isEmpty())
  }

  @Test(expected = IllegalStateException::class)
  fun whenTransformThrows_exceptionPropagates() = runTest {
    listOf(1, 2, 3).mapParallelSolution(concurrency = 2) { value ->
      if (value == 2) error("boom") else value
    }
  }
}
