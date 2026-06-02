package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.coroutines.cancellation.CancellationException

/**
 * Reference solution for EXERCISE 08.
 *
 * Each block runs in its own child coroutine. The first successful block completes the shared
 * [CompletableDeferred] (later completions are no-ops). Failures are recorded; when the last block
 * fails, the deferred is completed exceptionally with the first failure. After awaiting the result
 * we cancel every child so no losing block keeps running. CancellationException is rethrown inside
 * each child so a cancelled loser is never mistaken for a failure.
 */
suspend fun <T> raceFirstSuccessfulSolution(blocks: List<suspend () -> T>): T {
  require(blocks.isNotEmpty()) { "blocks must not be empty" }

  return coroutineScope {
    val firstSuccess = CompletableDeferred<T>()
    val failures = CopyOnWriteArrayList<Throwable>()

    val jobs = blocks.map { block ->
      launch {
        try {
          firstSuccess.complete(block())
        } catch (cancellation: CancellationException) {
          throw cancellation
        } catch (error: Throwable) {
          failures.add(error)
          if (failures.size == blocks.size) {
            firstSuccess.completeExceptionally(failures.first())
          }
        }
      }
    }

    try {
      firstSuccess.await()
    } finally {
      jobs.forEach { it.cancel() }
    }
  }
}
