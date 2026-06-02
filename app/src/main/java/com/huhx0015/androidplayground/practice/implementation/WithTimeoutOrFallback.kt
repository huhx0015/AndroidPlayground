package com.huhx0015.androidplayground.practice.implementation

/**
 * EXERCISE 06 — Timeout with a fallback value.
 *
 * Implement [withTimeoutOrFallback] so it runs [block] and:
 *  - returns the block's result if it finishes within [timeoutMs], or
 *  - returns [fallback] if the block takes longer than [timeoutMs].
 *
 * Contract:
 *  - When the timeout fires, the slow [block] must be CANCELLED (don't leave it running).
 *  - Only a timeout yields the fallback — a real exception thrown by [block] should propagate.
 *
 * Hints: kotlinx.coroutines.withTimeout + catch TimeoutCancellationException, or withTimeoutOrNull.
 * Prefer catching TimeoutCancellationException so you don't confuse a timeout with a legitimately
 * null result.
 *
 * The provided test class is [WithTimeoutOrFallbackTest] (currently @Ignore'd — remove to start).
 */
suspend fun <T> withTimeoutOrFallback(
  timeoutMs: Long,
  fallback: T,
  block: suspend () -> T,
): T {
  TODO("Implement withTimeoutOrFallback — see the KDoc contract above")
}
