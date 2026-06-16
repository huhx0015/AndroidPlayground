package com.huhx0015.androidplayground.practice.debugging

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

/**
 * DEBUGGING EXERCISE D6 — Flow context preservation.
 *
 * The test [com.huhx0015.androidplayground.practice.debugging.BuggyTemperatureFeedTest] is failing.
 * Find and fix the bug in [readings] WITHOUT modifying the test.
 *
 * Symptom: collecting [readings] throws "Flow invariant is violated" instead of emitting 18, 20, 22.
 * The intent is to do the (pretend CPU-bound) work off the collector's dispatcher, but the way the
 * dispatcher is changed is illegal for a flow.
 *
 * (Reference fix + explanation: see solutions/TemperatureFeedFixed.kt — but try it yourself first.)
 */
class BuggyTemperatureFeed {

  fun readings(): Flow<Int> = flow {
    for (celsius in listOf(18, 20, 22)) {
      withContext(Dispatchers.Default) {
        emit(celsius)
      }
    }
  }
}
