package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

/**
 * Reference solution for EXERCISE 06.
 *
 * withTimeout cancels [block] when the deadline passes and throws TimeoutCancellationException,
 * which we catch to return the fallback. Catching that specific type (rather than withTimeoutOrNull)
 * keeps a genuinely-null block result distinct from a timeout, and lets any real exception from
 * [block] propagate untouched.
 */
suspend fun <T> withTimeoutOrFallbackSolution(
  timeoutMs: Long,
  fallback: T,
  block: suspend () -> T,
): T = try {
  withTimeout(timeoutMs) { block() }
} catch (timeout: TimeoutCancellationException) {
  fallback
}
