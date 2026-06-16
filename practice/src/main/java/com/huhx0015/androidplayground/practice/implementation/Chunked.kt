package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.flow.Flow

/**
 * EXERCISE 05 — Time-and-size batching (the hard one).
 *
 * Implement [chunked] to group upstream items into batches. A batch is emitted when EITHER:
 *  - it reaches [maxSize] items, OR
 *  - [maxDelayMs] has elapsed since the batch's first item arrived,
 * whichever happens first.
 *
 * Contract:
 *  - Never emit an empty batch.
 *  - When upstream completes, emit any items still buffered.
 *  - Reaching [maxSize] should emit immediately and reset the timer for the next batch.
 *  - Cancellation of the collector must cancel upstream collection and the timer.
 *
 * Hints: channelFlow { } owns a buffer; a launched timer coroutine calls delay(maxDelayMs) then
 * flushes; guard the shared buffer with a Mutex so the timer and the collector don't race. Cancel
 * the timer when a size-triggered flush happens.
 *
 * The provided test class is [ChunkedTest] (currently @Ignore'd — remove to start).
 */
fun <T> Flow<T>.chunked(maxSize: Int, maxDelayMs: Long): Flow<List<T>> {
  TODO("Implement chunked — see the KDoc contract above")
}
