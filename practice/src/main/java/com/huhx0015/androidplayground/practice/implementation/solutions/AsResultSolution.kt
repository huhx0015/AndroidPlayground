package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Reference solution for EXERCISE 09.
 *
 * `map` wraps each value as a success. `catch` intercepts only UPSTREAM failures and turns the
 * terminal exception into one final `Result.failure`, after which the flow completes normally so the
 * collector never throws. `catch` already re-throws CancellationException, so cooperative
 * cancellation keeps propagating. (`kotlin.Result` is `out T`, so `Result.failure(...)` fits a
 * `Flow<Result<T>>` cleanly.)
 */
fun <T> Flow<T>.asResultSolution(): Flow<Result<T>> =
  map { Result.success(it) }
    .catch { emit(Result.failure(it)) }
