package com.huhx0015.androidplayground.practice.implementation

/**
 * EXERCISE 3 — Structured concurrency: parallelMap.
 *
 * Implement a suspending `map` that runs [transform] for every element CONCURRENTLY.
 *
 * Behaviour contract:
 *  - All transforms run in parallel (not sequentially): N tasks that each take 100ms
 *    should complete in ~100ms total, not N * 100ms.
 *  - The returned list preserves the ORIGINAL input order, regardless of which
 *    transform finishes first.
 *  - Fail-fast: if any transform throws, the others must be CANCELLED and the
 *    exception must propagate out of [parallelMap] (structured concurrency — no
 *    orphaned/leaked coroutines).
 *  - An empty input returns an empty list.
 *
 * Hints:
 *  - `coroutineScope { ... }` establishes a scope whose failure cancels its children.
 *  - `map { async { ... } }` launches the work; `awaitAll()` joins and rethrows.
 */
suspend fun <T, R> List<T>.parallelMap(transform: suspend (T) -> R): List<R> {
  TODO("Implement parallelMap — see the KDoc contract above")
}
