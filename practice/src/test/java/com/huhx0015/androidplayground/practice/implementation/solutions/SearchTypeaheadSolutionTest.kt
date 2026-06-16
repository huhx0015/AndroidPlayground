package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Verifies the reference solution for EXERCISE 10. Mirrors
 * [com.huhx0015.androidplayground.practice.implementation.SearchTypeaheadTest].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SearchTypeaheadSolutionTest {

  @Test
  fun debouncesRapidQueriesToTheFinalTerm() = runTest {
    val queries = flow {
      emit("a"); delay(10)
      emit("ab"); delay(10)
      emit("abc")
    }

    val results = queries.searchTypeaheadSolution(debounceMs = 50) { query -> "result:$query" }.toList()

    assertEquals(listOf("result:abc"), results)
  }

  @Test
  fun latestQueryCancelsTheInFlightSearch() = runTest {
    val started = mutableListOf<String>()
    val completed = mutableListOf<String>()

    val queries = flow {
      emit("first"); delay(100)
      emit("second")
    }

    val results = queries.searchTypeaheadSolution(debounceMs = 50) { query ->
      started += query
      delay(80)
      completed += query
      "done:$query"
    }.toList()

    assertEquals(listOf("done:second"), results)
    assertEquals(listOf("first", "second"), started)
    assertEquals(listOf("second"), completed)
  }
}
