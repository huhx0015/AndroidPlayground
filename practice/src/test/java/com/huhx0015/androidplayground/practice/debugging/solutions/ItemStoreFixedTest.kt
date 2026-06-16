package com.huhx0015.androidplayground.practice.debugging.solutions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Verifies the reference fix for D5. Mirrors
 * [com.huhx0015.androidplayground.practice.debugging.BuggyItemStoreTest] and passes.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ItemStoreFixedTest {

  @Test
  fun emitsAnImmutableSnapshotForEveryAdd() = runTest {
    val store = ItemStoreFixed()
    val observed = mutableListOf<List<String>>()

    val job = launch { store.state.collect { observed.add(it) } }
    runCurrent()

    store.add("a"); runCurrent()
    store.add("b"); runCurrent()
    store.add("c"); runCurrent()

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
