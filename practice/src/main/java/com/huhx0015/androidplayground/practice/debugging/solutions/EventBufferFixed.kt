package com.huhx0015.androidplayground.practice.debugging.solutions

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Reference fix for DEBUGGING EXERCISE D4.
 *
 * ROOT CAUSE: `conflate()` keeps only the LATEST value while the slow `map` is busy. The producer
 * emits the next events during that 100ms processing step, so the in-between values are conflated
 * away and never processed.
 *
 * FIX: remove `conflate()` so the slow processor back-pressures the producer and every event is
 * delivered. (If the producer truly must run ahead without loss, use `.buffer()` instead — it keeps
 * all items. `conflate`/`debounce`/`sample` are lossy by design.)
 */
class EventBufferFixed {

  fun processEvents(events: Flow<Int>): Flow<Int> =
    events.map { event ->
      delay(100)
      event
    }
}
