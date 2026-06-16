package com.huhx0015.androidplayground.practice.debugging

import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

/**
 * DEBUGGING CHALLENGE 2 — ParallelAggregator.
 *
 * Intended behaviour (the spec the test encodes):
 *  - Run every task concurrently and return the SUM of their results.
 *  - Fail-fast: if ANY task throws, [aggregate] must propagate that exception
 *    (it must NOT silently return a partial sum).
 *
 * The corresponding test (`ParallelAggregatorTest`) is currently FAILING.
 * Find and fix the bug WITHOUT modifying the test.
 */
suspend fun aggregate(tasks: List<suspend () -> Int>): Int = supervisorScope {
  val deferreds = tasks.map { task -> async { task() } }

  var sum = 0
  for (deferred in deferreds) {
    try {
      sum += deferred.await()
    } catch (e: Exception) {
      // Skip failed tasks and keep going.
    }
  }
  sum
}
