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
 * Tests for EXERCISE 10 ([searchTypeahead]). Remove @Ignore to start.
 */
@Ignore("Remove this annotation to begin Exercise 10")
@OptIn(ExperimentalCoroutinesApi::class)
class SearchTypeaheadTest {

  @Test
  fun debouncesRapidQueriesToTheFinalTerm() = runTest {
    val queries = flow {
      emit("a"); delay(10)
      emit("ab"); delay(10)
      emit("abc") // burst within the 50ms window -> only "abc" survives
    }

    val results = queries.searchTypeahead(debounceMs = 50) { query -> "result:$query" }.toList()

    assertEquals(listOf("result:abc"), results)
  }

  @Test
  fun latestQueryCancelsTheInFlightSearch() = runTest {
    val started = mutableListOf<String>()
    val completed = mutableListOf<String>()

    val queries = flow {
      emit("first"); delay(100) // survives debounce at t=50, search starts
      emit("second") // at t=100 cancels the in-flight "first" search
    }

    val results = queries.searchTypeahead(debounceMs = 50) { query ->
      started += query
      delay(80)
      completed += query
      "done:$query"
    }.toList()

    assertEquals(listOf("done:second"), results)
    assertEquals(listOf("first", "second"), started)
    assertEquals(listOf("second"), completed) // "first" was cancelled mid-flight
  }
}
