package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Verifies the reference solution for EXERCISE 05. Mirrors
 * [com.huhx0015.androidplayground.practice.implementation.ChunkedTest].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ChunkedSolutionTest {

  @Test
  fun emitsBatchWhenSizeReached_andRemainderOnCompletion() = runTest {
    val upstream = flow {
      emit(1); emit(2); emit(3); emit(4); emit(5)
    }

    val result = upstream.chunkedSolution(maxSize = 2, maxDelayMs = 1_000).toList()

    assertEquals(listOf(listOf(1, 2), listOf(3, 4), listOf(5)), result)
  }

  @Test
  fun emitsBatchWhenTimeoutElapses() = runTest {
    val upstream = flow {
      emit(1)
      delay(50); emit(2)
      delay(200); emit(3)
    }

    val result = upstream.chunkedSolution(maxSize = 10, maxDelayMs = 100).toList()

    assertEquals(listOf(listOf(1, 2), listOf(3)), result)
  }

  @Test
  fun emptyUpstream_emitsNothing() = runTest {
    val result = flow<Int> { }.chunkedSolution(maxSize = 3, maxDelayMs = 100).toList()

    assertEquals(emptyList<List<Int>>(), result)
  }
}
