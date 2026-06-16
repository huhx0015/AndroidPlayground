package com.huhx0015.androidplayground.practice.debugging

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map

/**
 * DEBUGGING EXERCISE D4 — Dropped emissions under backpressure.
 *
 * The test [com.huhx0015.androidplayground.practice.debugging.BuggyEventBufferTest] is failing.
 * Find and fix the bug in [processEvents] WITHOUT modifying the test.
 *
 * Symptom: [processEvents] applies a slow per-item step and must deliver EVERY event, in order, even
 * when the producer emits faster than the processor consumes. Instead, intermediate events vanish.
 *
 * (Reference fix + explanation: see solutions/EventBufferFixed.kt — but try it yourself first.)
 */
class BuggyEventBuffer {

  fun processEvents(events: Flow<Int>): Flow<Int> =
    events
      .conflate()
      .map { event ->
        delay(100) // slow processing step
        event
      }
}
