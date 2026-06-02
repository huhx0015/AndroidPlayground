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
 * Tests for EXERCISE 05 ([chunked]). Remove @Ignore to start.
 */
@Ignore("Remove this annotation to begin Exercise 05")
@OptIn(ExperimentalCoroutinesApi::class)
class ChunkedTest {

  @Test
  fun emitsBatchWhenSizeReached_andRemainderOnCompletion() = runTest {
    val upstream = flow {
      emit(1); emit(2); emit(3); emit(4); emit(5)
    }

    val result = upstream.chunked(maxSize = 2, maxDelayMs = 1_000).toList()

    assertEquals(listOf(listOf(1, 2), listOf(3, 4), listOf(5)), result)
  }

  @Test
  fun emitsBatchWhenTimeoutElapses() = runTest {
    val upstream = flow {
      emit(1)
      delay(50); emit(2)
      delay(200); emit(3) // 200ms gap > maxDelayMs, so [1, 2] flushes by timeout first
    }

    val result = upstream.chunked(maxSize = 10, maxDelayMs = 100).toList()

    assertEquals(listOf(listOf(1, 2), listOf(3)), result)
  }

  @Test
  fun emptyUpstream_emitsNothing() = runTest {
    val result = flow<Int> { }.chunked(maxSize = 3, maxDelayMs = 100).toList()

    assertEquals(emptyList<List<Int>>(), result)
  }
}
