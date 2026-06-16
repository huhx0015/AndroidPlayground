package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

/**
 * Tests for EXERCISE 07 ([computeTotal]). Remove @Ignore to start.
 */
@Ignore("Remove this annotation to begin Exercise 07")
@OptIn(ExperimentalCoroutinesApi::class)
class ComputeTotalTest {

  @Test
  fun reEmitsWheneverEitherInputChanges() = runTest {
    val quantity = flow {
      emit(1)             // t = 0   (price not seen yet -> no emit)
      delay(100); emit(2) // t = 100 -> 2 * 10.0 = 20.0
      delay(100); emit(3) // t = 200 -> 3 * 20.0 = 60.0
    }
    val price = flow {
      delay(50); emit(10.0)  // t = 50  -> 1 * 10.0 = 10.0
      delay(100); emit(20.0) // t = 150 -> 2 * 20.0 = 40.0
    }

    val result = computeTotal(quantity, price).toList()

    assertEquals(listOf(10.0, 20.0, 40.0, 60.0), result)
  }

  @Test
  fun emitsNothingUntilBothFlowsEmit() = runTest {
    val quantity = flow { emit(5) }
    val price = flow<Double> { } // never emits

    val result = computeTotal(quantity, price).toList()

    assertEquals(emptyList<Double>(), result)
  }
}
