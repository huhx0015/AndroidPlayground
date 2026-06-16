package com.huhx0015.androidplayground.practice.debugging

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map

/**
 * DEBUGGING CHALLENGE 3 — EventBuffer.
 *
 * Intended behaviour (the spec the test encodes):
 *  - [processEvents] applies a slow per-item processing step (here simulated with a
 *    delay) and must deliver EVERY event, in order. No events may be dropped, even
 *    though the producer emits faster than the processor consumes.
 *
 * The corresponding test (`EventBufferTest`) is currently FAILING.
 * Find and fix the bug WITHOUT modifying the test.
 */
fun processEvents(events: Flow<Int>): Flow<Int> =
  events
    .conflate()
    .map { event ->
      delay(100) // slow processing step
      event
    }
