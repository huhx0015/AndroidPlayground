package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

/**
 * Reference solution for EXERCISE 01.
 *
 * Order is preserved because [map] keeps the source order and [awaitAll] returns results in the
 * same order the deferreds were created. The [Semaphore] caps how many transforms hold a permit
 * (and therefore run) at once. coroutineScope ties every child to the caller: the first failure
 * cancels the siblings and propagates out, so nothing is left running.
 */
suspend fun <T, R> Iterable<T>.mapParallelSolution(
  concurrency: Int,
  transform: suspend (T) -> R,
): List<R> {
  require(concurrency >= 1) { "concurrency must be >= 1 but was $concurrency" }
  val semaphore = Semaphore(concurrency)
  return coroutineScope {
    this@mapParallelSolution
      .map { item ->
        async { semaphore.withPermit { transform(item) } }
      }
      .awaitAll()
  }
}
