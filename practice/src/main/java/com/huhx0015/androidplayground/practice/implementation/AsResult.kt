package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.flow.Flow

/**
 * EXERCISE 09 — Error handling in flows: asResult.
 *
 * Implement [asResult] so that values and a terminal failure are represented as `Result<T>`
 * emissions, and the collector NEVER sees an exception thrown.
 *
 * Contract:
 *  - Each successful upstream value `v` is emitted as `Result.success(v)`.
 *  - If the upstream throws, emit a single `Result.failure(throwable)` and then complete the flow
 *    NORMALLY (the collector must not crash).
 *  - Values already emitted before the failure are preserved, in order.
 *  - Cooperative cancellation (CancellationException) must NOT be swallowed — it has to keep
 *    propagating so structured concurrency still works.
 *
 * Hints: `map { Result.success(it) }` then `catch { emit(Result.failure(it)) }`. The `catch`
 * operator already ignores CancellationException for you.
 *
 * The provided test class is [AsResultTest] (currently @Ignore'd — remove to start).
 */
fun <T> Flow<T>.asResult(): Flow<Result<T>> {
  TODO("Implement asResult — see the KDoc contract above")
}
