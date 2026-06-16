package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.flow.Flow

/**
 * EXERCISE 2 — Custom Flow operator: retryWithBackoff.
 *
 * Implement a retry operator with exponential backoff.
 *
 * Behaviour contract:
 *  - [maxAttempts] is the TOTAL number of attempts, including the initial one
 *    (so the number of retries is maxAttempts - 1). Must be >= 1.
 *  - When the upstream flow throws, wait, then re-subscribe and try again.
 *  - The delay before retry number `r` (0-based) is:
 *        initialDelayMillis * (factor ^ r)
 *    i.e. first retry waits initialDelayMillis, second waits initialDelayMillis * factor, etc.
 *  - After all attempts are exhausted, the LAST exception must be rethrown to the collector.
 *  - A successful emission must pass through unchanged.
 *  - Use the coroutine-aware delay so virtual time works under `runTest`.
 *
 * Hint: `kotlinx.coroutines.flow.retryWhen { cause, attempt -> ... }` gives you a 0-based
 *       `attempt` index and lets you `delay(...)` before returning `true` to retry.
 *
 * @param maxAttempts total attempts including the first (>= 1)
 * @param initialDelayMillis base backoff delay in millis
 * @param factor multiplier applied per retry (e.g. 2.0 for doubling)
 */
fun <T> Flow<T>.retryWithBackoff(
  maxAttempts: Int,
  initialDelayMillis: Long,
  factor: Double = 2.0,
): Flow<T> {
  TODO("Implement retryWithBackoff — see the KDoc contract above")
}
