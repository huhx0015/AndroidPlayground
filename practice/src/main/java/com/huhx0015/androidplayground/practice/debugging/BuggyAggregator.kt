package com.huhx0015.androidplayground.practice.debugging

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * DEBUGGING EXERCISE D2 — Work escaping its scope.
 *
 * The test [com.huhx0015.androidplayground.practice.debugging.BuggyAggregatorTest] is failing.
 * Find and fix the bug in [sumAll] WITHOUT modifying the test.
 *
 * Symptom: [sumAll] returns 0 because it returns before the launched work has produced any results.
 *
 * (Reference fix + explanation: see solutions/AggregatorFixed.kt — but try it yourself first.)
 */
class BuggyAggregator {

  /** Runs every task and returns the sum of their results. */
  @OptIn(DelicateCoroutinesApi::class)
  suspend fun sumAll(tasks: List<suspend () -> Int>): Int {
    val results = mutableListOf<Int>()
    GlobalScope.launch {
      tasks.forEach { task ->
        results.add(task())
      }
    }
    return results.sum()
  }
}
