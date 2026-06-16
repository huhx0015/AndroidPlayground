package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException

/**
 * Reference solution for EXERCISE 02.
 *
 * The CancellationException catch comes FIRST and rethrows — otherwise the broad `catch (Throwable)`
 * would swallow cooperative cancellation and keep retrying a coroutine that is meant to stop. We
 * skip the delay after the last attempt and rethrow the most recent error when attempts run out.
 */
suspend fun <T> retryWithBackoffSolution(
  maxAttempts: Int,
  initialDelayMs: Long,
  factor: Double,
  block: suspend () -> T,
): T {
  require(maxAttempts >= 1) { "maxAttempts must be >= 1 but was $maxAttempts" }
  var currentDelay = initialDelayMs
  var lastError: Throwable? = null

  repeat(maxAttempts) { attempt ->
    try {
      return block()
    } catch (cancellation: CancellationException) {
      throw cancellation
    } catch (error: Throwable) {
      lastError = error
      val isLastAttempt = attempt == maxAttempts - 1
      if (!isLastAttempt) {
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong()
      }
    }
  }

  throw lastError ?: IllegalStateException("retryWithBackoff completed without a result or error")
}
