package com.huhx0015.androidplayground.practice.debugging.solutions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Verifies the reference fix for D3. Mirrors
 * [com.huhx0015.androidplayground.practice.debugging.BuggyPriceCalculatorTest] and passes.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PriceCalculatorFixedTest {

  @Test
  fun reEmitsWheneverEitherInputChanges() = runTest {
    val quantity = flow {
      emit(2)
      delay(100); emit(3)
    }
    val unitPrice = flow {
      delay(50); emit(10.0)
      delay(100); emit(20.0)
    }

    val result = PriceCalculatorFixed().totals(quantity, unitPrice).toList()

    assertEquals(listOf(20.0, 30.0, 60.0), result)
  }
}
