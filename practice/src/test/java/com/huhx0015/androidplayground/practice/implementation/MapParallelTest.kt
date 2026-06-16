package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max

/**
 * Tests for EXERCISE 01 ([mapParallel]).
 *
 * Remove the @Ignore annotation below to start the exercise, watch these fail (the skeleton
 * throws via TODO), then implement [mapParallel] until they pass.
 */
@Ignore("Remove this annotation to begin Exercise 01")
@OptIn(ExperimentalCoroutinesApi::class)
class MapParallelTest {

  @Test
  fun preservesOrder_evenWhenLaterItemsFinishFirst() = runTest {
    val input = listOf(1, 2, 3, 4, 5)

    val result = input.mapParallel(concurrency = 2) { value ->
      // Earlier items take longer, so the result is only correct if order is preserved explicitly.
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

    (1..20).toList().mapParallel(concurrency = concurrency) { value ->
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
    val result = emptyList<Int>().mapParallel(concurrency = 4) { it * 2 }

    assertTrue(result.isEmpty())
  }

  @Test(expected = IllegalStateException::class)
  fun whenTransformThrows_exceptionPropagates() = runTest {
    listOf(1, 2, 3).mapParallel(concurrency = 2) { value ->
      if (value == 2) error("boom") else value
    }
  }
}
