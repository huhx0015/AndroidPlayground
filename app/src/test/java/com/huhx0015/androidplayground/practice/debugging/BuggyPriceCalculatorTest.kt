package com.huhx0015.androidplayground.practice.debugging

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

/**
 * DEBUGGING EXERCISE D3 test. Remove @Ignore to reproduce the failure, then fix
 * [BuggyPriceCalculator].
 */
@Ignore("Remove this annotation to work DEBUGGING EXERCISE D3")
@OptIn(ExperimentalCoroutinesApi::class)
class BuggyPriceCalculatorTest {

  @Test
  fun reEmitsWheneverEitherInputChanges() = runTest {
    val quantity = flow {
      emit(2)             // t = 0
      delay(100); emit(3) // t = 100 -> 3 * 10.0 = 30.0
    }
    val unitPrice = flow {
      delay(50); emit(10.0)  // t = 50  -> 2 * 10.0 = 20.0
      delay(100); emit(20.0) // t = 150 -> 3 * 20.0 = 60.0
    }

    val result = BuggyPriceCalculator().totals(quantity, unitPrice).toList()

    // combine re-emits on every change; zip would only yield [20.0, 60.0].
    assertEquals(listOf(20.0, 30.0, 60.0), result)
  }
}
