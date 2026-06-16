package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.flow.Flow

/**
 * EXERCISE 10 — Typeahead search: debounce + cancel the previous request.
 *
 * Implement [searchTypeahead] on a flow of query strings (e.g. keystrokes). It should:
 *  - Wait for the user to stop typing for [debounceMs] before issuing a request (debounce), so rapid
 *    keystrokes collapse to the final query.
 *  - When a new query passes the debounce while a [search] is still running, CANCEL the in-flight
 *    request and start one for the newer query (only the latest result matters).
 *  - Emit each completed search result.
 *
 * Example with debounceMs = 50: queries "a","ab","abc" typed 10ms apart collapse to a single
 * search("abc"). If "abc" then "abcd" arrive 100ms apart, the search for "abc" is cancelled when
 * "abcd" comes in.
 *
 * Hints: `debounce(debounceMs)` then `flatMapLatest { query -> flow { emit(search(query)) } }`.
 * `flatMapLatest` is exactly the "cancel the previous inner flow when a new value arrives" operator.
 *
 * The provided test class is [SearchTypeaheadTest] (currently @Ignore'd — remove to start).
 */
fun <T> Flow<String>.searchTypeahead(debounceMs: Long, search: suspend (String) -> T): Flow<T> {
  TODO("Implement searchTypeahead — see the KDoc contract above")
}
