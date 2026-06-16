package com.huhx0015.androidplayground.practice.debugging

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * DEBUGGING EXERCISE D5 — StateFlow that emits only once.
 *
 * The test [com.huhx0015.androidplayground.practice.debugging.BuggyItemStoreTest] is failing.
 * Find and fix the bug in [add] WITHOUT modifying the test.
 *
 * Symptom: every [add] should publish a new snapshot, so a collector observes the progression
 * [] -> [a] -> [a, b] -> [a, b, c]. Instead the collector only sees one change after the first add.
 *
 * (Reference fix + explanation: see solutions/ItemStoreFixed.kt — but try it yourself first.)
 */
class BuggyItemStore {

  private val items = mutableListOf<String>()

  private val _state = MutableStateFlow<List<String>>(emptyList())
  val state: StateFlow<List<String>> = _state.asStateFlow()

  fun add(item: String) {
    items.add(item)
    _state.value = items
  }
}
