package com.huhx0015.androidplayground.practice.implementation

/**
 * EXERCISE 01 — Parallel map with a concurrency limit.
 *
 * Implement [mapParallel] so that it applies [transform] to every element of the receiver
 * concurrently, while never running more than [concurrency] transforms at the same time.
 *
 * Contract:
 *  - The returned list MUST preserve the original iteration order, regardless of which
 *    transform finishes first.
 *  - At most [concurrency] invocations of [transform] may run at any single moment.
 *  - If any [transform] throws, the remaining in-flight work is cancelled and the exception
 *    propagates to the caller (structured concurrency — no orphaned coroutines).
 *  - An empty receiver returns an empty list without launching anything.
 *  - [concurrency] must be >= 1.
 *
 * Hints: coroutineScope { }, kotlinx.coroutines.sync.Semaphore + withPermit, async / awaitAll.
 *
 * The provided test class is [MapParallelTest] (currently @Ignore'd — remove the annotation to start).
 */
suspend fun <T, R> Iterable<T>.mapParallel(
  concurrency: Int,
  transform: suspend (T) -> R,
): List<R> {
  TODO("Implement mapParallel — see the KDoc contract above")
}
