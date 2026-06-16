package com.huhx0015.androidplayground.practice.debugging.solutions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Reference fix for DEBUGGING EXERCISE D6.
 *
 * ROOT CAUSE: a `flow { }` must emit from the SAME coroutine that runs the builder. Wrapping `emit`
 * in `withContext(Dispatchers.Default)` emits from a different context, which the flow detects and
 * rejects with "Flow invariant is violated". (The same happens if you emit from a `launch`.)
 *
 * FIX: never change context around `emit`. To run the upstream on another dispatcher, use the
 * `flowOn` operator — it shifts the producer's context correctly while emissions still happen on the
 * flow's own coroutine.
 */
class TemperatureFeedFixed {

  fun readings(): Flow<Int> = flow {
    for (celsius in listOf(18, 20, 22)) {
      emit(celsius)
    }
  }.flowOn(Dispatchers.Default)
}
