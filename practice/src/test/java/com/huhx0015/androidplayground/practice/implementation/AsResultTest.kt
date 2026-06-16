package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test

/**
 * Tests for EXERCISE 09 ([asResult]). Remove @Ignore to start.
 */
@Ignore("Remove this annotation to begin Exercise 09")
@OptIn(ExperimentalCoroutinesApi::class)
class AsResultTest {

  @Test
  fun wrapsEachEmissionInResultSuccess() = runTest {
    val results = flowOf(1, 2, 3).asResult().toList()

    assertEquals(listOf(1, 2, 3), results.map { it.getOrThrow() })
    assertTrue(results.all { it.isSuccess })
  }

  @Test
  fun capturesUpstreamFailureAndCompletesNormally() = runTest {
    val upstream = flow {
      emit(1)
      emit(2)
      throw IllegalStateException("boom")
    }

    val results = upstream.asResult().toList()

    assertEquals(3, results.size)
    assertEquals(1, results[0].getOrThrow())
    assertEquals(2, results[1].getOrThrow())
    assertTrue(results[2].isFailure)
    assertEquals("boom", results[2].exceptionOrNull()?.message)
  }
}
