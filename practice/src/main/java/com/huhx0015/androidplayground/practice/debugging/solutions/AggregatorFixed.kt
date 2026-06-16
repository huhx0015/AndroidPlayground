package com.huhx0015.androidplayground.practice.debugging.solutions

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Reference fix for DEBUGGING EXERCISE D2.
 *
 * ROOT CAUSE: the work was launched into GlobalScope and never awaited. `GlobalScope.launch` returns
 * immediately, so `results.sum()` runs while `results` is still empty and the function returns 0.
 * GlobalScope also detaches the work from the caller's lifecycle (a leak) and from cancellation.
 *
 * FIX: use coroutineScope to tie the children to the caller, run the tasks with async, and awaitAll
 * before summing. This also makes cancellation and error propagation correct.
 */
class AggregatorFixed {

  suspend fun sumAll(tasks: List<suspend () -> Int>): Int = coroutineScope {
    tasks
      .map { task -> async { task() } }
      .awaitAll()
      .sum()
  }
}
