package com.huhx0015.androidplayground.practice.implementation

/**
 * EXERCISE 02 — Retry with exponential backoff.
 *
 * Implement [retryWithBackoff] so that it invokes [block], and on failure retries it up to a total
 * of [maxAttempts] times, waiting longer between each attempt.
 *
 * Contract:
 *  - Run [block]; if it returns, return that value immediately.
 *  - On failure, wait [initialDelayMs], then retry. Multiply the delay by [factor] before each
 *    subsequent wait (e.g. 100, 200, 400 ... for factor = 2.0).
 *  - Make at most [maxAttempts] total attempts. Do NOT delay after the final attempt.
 *  - If every attempt fails, rethrow the exception from the LAST attempt.
 *  - A CancellationException must never be retried — let it propagate.
 *  - [maxAttempts] must be >= 1.
 *
 * Hints: a retry loop, try/catch, kotlinx.coroutines.delay. Catch CancellationException separately
 * and rethrow it before catching broader Throwable.
 *
 * The provided test class is [RetryWithBackoffTest] (currently @Ignore'd — remove to start).
 */
suspend fun <T> retryWithBackoff(
  maxAttempts: Int,
  initialDelayMs: Long,
  factor: Double,
  block: suspend () -> T,
): T {
  TODO("Implement retryWithBackoff — see the KDoc contract above")
}
