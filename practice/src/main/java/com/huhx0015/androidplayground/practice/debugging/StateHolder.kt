package com.huhx0015.androidplayground.practice.debugging

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * DEBUGGING CHALLENGE 1 — StateHolder.
 *
 * Intended behaviour (this is the spec the test encodes):
 *  - [StateHolder] exposes an immutable list of items as a [StateFlow].
 *  - Every call to [add] should publish a NEW snapshot, so a collector observes the
 *    full progression: [] -> [a] -> [a, b] -> [a, b, c].
 *
 * The corresponding test (`StateHolderTest`) is currently FAILING.
 * Find and fix the bug WITHOUT modifying the test.
 */
class StateHolder {

  private val items = mutableListOf<String>()

  private val _state = MutableStateFlow<List<String>>(emptyList())
  val state: StateFlow<List<String>> = _state.asStateFlow()

  fun add(item: String) {
    items.add(item)
    _state.value = items
  }
}
