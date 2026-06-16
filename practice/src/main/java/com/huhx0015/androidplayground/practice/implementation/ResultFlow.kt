package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.flow.Flow

/**
 * EXERCISE 5 — Error handling in flows: asResult.
 *
 * Wrap a `Flow<T>` so that every value and any terminal failure are represented as
 * `Result<T>` emissions, and the collector NEVER sees an exception thrown.
 *
 * Behaviour contract:
 *  - Each successful upstream value `v` is emitted as `Result.success(v)`.
 *  - If the upstream throws, emit a single `Result.failure(throwable)` and then
 *    complete the flow NORMALLY (the collector must not crash).
 *  - Values already emitted before the failure are preserved (in order).
 *  - Cooperative cancellation (CancellationException) must NOT be swallowed — it should
 *    propagate so structured concurrency keeps working.
 *
 * Hints:
 *  - `map { Result.success(it) }` then `catch { emit(Result.failure(it)) }`.
 *  - The `catch` operator already ignores CancellationException for you.
 */
fun <T> Flow<T>.asResult(): Flow<Result<T>> {
  TODO("Implement asResult — see the KDoc contract above")
}
