package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

/**
 * Reference solution for EXERCISE 10.
 *
 * `debounce` gates the query stream — it only forwards a value once [debounceMs] has passed with no
 * newer query, so bursts of keystrokes collapse to the final term. `flatMapLatest` then maps each
 * surviving query to an inner search flow and AUTOMATICALLY cancels the previous inner flow when a
 * newer query arrives, so a slow request for a stale query is abandoned.
 */
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
fun <T> Flow<String>.searchTypeaheadSolution(
  debounceMs: Long,
  search: suspend (String) -> T,
): Flow<T> =
  debounce(debounceMs)
    .flatMapLatest { query -> flow { emit(search(query)) } }
