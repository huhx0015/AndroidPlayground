package com.huhx0015.androidplayground.practice.debugging

import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * ============================================================================
 *  DO NOT MODIFY THIS TEST. Fix the bug in StateHolder so that it passes.
 * ============================================================================
 */
class StateHolderTest {

  @Test
  fun `emits an immutable snapshot for every add`() = runTest {
    val holder = StateHolder()
    val observed = mutableListOf<List<String>>()

    val job = launch { holder.state.collect { observed.add(it) } }
    runCurrent()

    holder.add("a")
    runCurrent()
    holder.add("b")
    runCurrent()
    holder.add("c")
    runCurrent()

    job.cancel()

    assertEquals(
      listOf(
        emptyList(),
        listOf("a"),
        listOf("a", "b"),
        listOf("a", "b", "c"),
      ),
      observed,
    )
  }
}
