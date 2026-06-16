package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.flow.Flow

/**
 * EXERCISE 04 — Cache-then-network with graceful fallback.
 *
 * Implement [cacheThenNetwork] to return a [Flow] that:
 *  1. Emits the value from [cache] first (fast, local).
 *  2. Then attempts [network] and emits its (fresh) value.
 *
 * Contract:
 *  - Always emit the cached value before attempting the network.
 *  - If [network] fails, do NOT propagate the error: the cached value already emitted is the
 *    fallback, so the flow completes normally having emitted only the cached value.
 *  - A CancellationException must still propagate (never swallow cooperative cancellation).
 *
 * Hints: flow { }, emit cache, then try/catch around emit(network()). Catch CancellationException
 * separately and rethrow it before catching broader Throwable.
 *
 * The provided test class is [CacheThenNetworkTest] (currently @Ignore'd — remove to start).
 */
fun <T> cacheThenNetwork(
  cache: suspend () -> T,
  network: suspend () -> T,
): Flow<T> {
  TODO("Implement cacheThenNetwork — see the KDoc contract above")
}
