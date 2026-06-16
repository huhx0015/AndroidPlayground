package com.huhx0015.androidplayground.practice.debugging

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

/**
 * DEBUGGING EXERCISE D5 test. Remove @Ignore to reproduce the failure, then fix [BuggyItemStore].
 */
@Ignore("Remove this annotation to work DEBUGGING EXERCISE D5")
@OptIn(ExperimentalCoroutinesApi::class)
class BuggyItemStoreTest {

  @Test
  fun emitsAnImmutableSnapshotForEveryAdd() = runTest {
    val store = BuggyItemStore()
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
