package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

/**
 * Reference solution for EXERCISE 03.
 *
 * A `canEmit` gate starts open. When an item passes, we emit it, close the gate, and launch a
 * child coroutine that re-opens it after [windowMs]. Items that arrive while the gate is closed are
 * dropped. Using channelFlow + a launched timer keeps everything on the coroutine clock, so it is
 * deterministic under runTest's virtual time.
 */
fun <T> Flow<T>.throttleFirstSolution(windowMs: Long): Flow<T> = channelFlow {
  var canEmit = true
  collect { value ->
    if (canEmit) {
      send(value)
      canEmit = false
      launch {
        kotlinx.coroutines.delay(windowMs)
        canEmit = true
      }
    }
  }
}
