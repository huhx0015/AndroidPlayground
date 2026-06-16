package com.huhx0015.androidplayground.practice.debugging.solutions

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Reference fix for DEBUGGING EXERCISE D5.
 *
 * ROOT CAUSE: `_state.value = items` stores the SAME `MutableList` instance every time. `StateFlow`
 * only emits when the new value is `!=` the previous one — but the previous value IS that same
 * object, now mutated, so it always compares equal and the collector sees nothing after the first
 * change. (Snapshots a collector already captured also keep mutating, because they alias the live
 * list.)
 *
 * FIX: publish a fresh immutable snapshot each time (`items.toList()`). Equivalently, drop the
 * backing list and use `_state.update { it + item }`.
 */
class ItemStoreFixed {

  private val items = mutableListOf<String>()

  private val _state = MutableStateFlow<List<String>>(emptyList())
  val state: StateFlow<List<String>> = _state.asStateFlow()

  fun add(item: String) {
    items.add(item)
    _state.value = items.toList()
  }
}
