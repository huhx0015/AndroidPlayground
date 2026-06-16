package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultFlowTest {

  @Test
  fun `wraps each emission in Result success`() = runTest {
    val results = flowOf(1, 2, 3).asResult().toList()

    assertEquals(3, results.size)
    assertEquals(listOf(1, 2, 3), results.map { it.getOrThrow() })
    assertTrue(results.all { it.isSuccess })
  }

  @Test
  fun `captures upstream failure as Result failure and completes normally`() = runTest {
    val upstream = flow {
      emit(1)
      emit(2)
      throw IllegalStateException("upstream exploded")
    }

    // The collector must NOT throw — toList() should complete.
    val results = upstream.asResult().toList()

    assertEquals(3, results.size)
    assertEquals(1, results[0].getOrThrow())
    assertEquals(2, results[1].getOrThrow())
    assertTrue("last item should be a failure", results[2].isFailure)
    assertEquals(
      "upstream exploded",
      results[2].exceptionOrNull()?.message,
    )
  }
}
