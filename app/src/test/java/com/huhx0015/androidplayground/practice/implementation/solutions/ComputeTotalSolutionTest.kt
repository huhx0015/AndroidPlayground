package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Verifies the reference solution for EXERCISE 07. Mirrors
 * [com.huhx0015.androidplayground.practice.implementation.ComputeTotalTest].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ComputeTotalSolutionTest {

  @Test
  fun reEmitsWheneverEitherInputChanges() = runTest {
    val quantity = flow {
      emit(1)
      delay(100); emit(2)
      delay(100); emit(3)
    }
    val price = flow {
      delay(50); emit(10.0)
      delay(100); emit(20.0)
    }

    val result = computeTotalSolution(quantity, price).toList()

    assertEquals(listOf(10.0, 20.0, 40.0, 60.0), result)
  }

  @Test
  fun emitsNothingUntilBothFlowsEmit() = runTest {
    val quantity = flow { emit(5) }
    val price = flow<Double> { }

    val result = computeTotalSolution(quantity, price).toList()

    assertEquals(emptyList<Double>(), result)
  }
}
