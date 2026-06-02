package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.cancellation.CancellationException

/**
 * Reference solution for EXERCISE 04.
 *
 * Emit the cache value unconditionally, then try the network. A network failure is swallowed so the
 * already-emitted cache value stands as the fallback — but CancellationException is rethrown first
 * so cancellation is never mistaken for a network error.
 */
fun <T> cacheThenNetworkSolution(
  cache: suspend () -> T,
  network: suspend () -> T,
): Flow<T> = flow {
  emit(cache())
  try {
    emit(network())
  } catch (cancellation: CancellationException) {
    throw cancellation
  } catch (error: Throwable) {
    // Network failed — the cached value already emitted is the fallback. Complete normally.
  }
}
