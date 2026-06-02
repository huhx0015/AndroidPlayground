package com.huhx0015.androidplayground.practice.runner

import com.huhx0015.androidplayground.practice.debugging.BuggyAggregator
import com.huhx0015.androidplayground.practice.debugging.BuggyDataLoader
import com.huhx0015.androidplayground.practice.debugging.BuggyPriceCalculator
import com.huhx0015.androidplayground.practice.debugging.solutions.AggregatorFixed
import com.huhx0015.androidplayground.practice.debugging.solutions.DataLoaderFixed
import com.huhx0015.androidplayground.practice.debugging.solutions.PriceCalculatorFixed
import com.huhx0015.androidplayground.practice.implementation.cacheThenNetwork
import com.huhx0015.androidplayground.practice.implementation.chunked
import com.huhx0015.androidplayground.practice.implementation.computeTotal
import com.huhx0015.androidplayground.practice.implementation.mapParallel
import com.huhx0015.androidplayground.practice.implementation.raceFirstSuccessful
import com.huhx0015.androidplayground.practice.implementation.retryWithBackoff
import com.huhx0015.androidplayground.practice.implementation.throttleFirst
import com.huhx0015.androidplayground.practice.implementation.withTimeoutOrFallback
import com.huhx0015.androidplayground.practice.implementation.solutions.cacheThenNetworkSolution
import com.huhx0015.androidplayground.practice.implementation.solutions.chunkedSolution
import com.huhx0015.androidplayground.practice.implementation.solutions.computeTotalSolution
import com.huhx0015.androidplayground.practice.implementation.solutions.mapParallelSolution
import com.huhx0015.androidplayground.practice.implementation.solutions.raceFirstSuccessfulSolution
import com.huhx0015.androidplayground.practice.implementation.solutions.retryWithBackoffSolution
import com.huhx0015.androidplayground.practice.implementation.solutions.throttleFirstSolution
import com.huhx0015.androidplayground.practice.implementation.solutions.withTimeoutOrFallbackSolution
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

/** Whether an exercise is an implementation challenge or a debugging challenge. */
enum class ExerciseCategory(val label: String) {
  IMPLEMENTATION("Implementation challenge"),
  DEBUGGING("Debugging challenge"),
}

/**
 * One runnable practice exercise.
 *
 * [runSkeleton] invokes the code you edit (the `TODO()` skeleton for implementation challenges, or
 * the buggy class for debugging challenges). [runSolution] invokes the reference answer. Each lambda
 * runs a representative scenario, verifies the output, and returns a short success message — or
 * throws (AssertionError on a wrong result, NotImplementedError while still a TODO).
 */
data class PracticeExercise(
  val id: String,
  val number: String,
  val title: String,
  val category: ExerciseCategory,
  val concept: String,
  val sourceHint: String,
  val runSkeleton: suspend () -> String,
  val runSolution: suspend () -> String,
)

/** Throws an [AssertionError] (shown as a FAILED result) when the output doesn't match. */
private fun verifyEquals(expected: Any?, actual: Any?) {
  if (expected != actual) throw AssertionError("expected $expected but was $actual")
}

/**
 * Drives the D1 cancellation scenario: start [loadOrDefault] in a child coroutine, let it suspend,
 * then cancel it. A correct implementation lets the cancellation propagate (so the assignment never
 * runs and the returned value stays null); the buggy one swallows it and returns the default.
 */
private suspend fun runCancellationScenario(
  loadOrDefault: suspend (default: String, load: suspend () -> String) -> String,
): String? {
  var returned: String? = null
  coroutineScope {
    val job = launch {
      returned = loadOrDefault("default") { delay(200); "loaded" }
    }
    delay(50)
    job.cancel()
    job.join()
  }
  return returned
}

/**
 * The full set of exercises surfaced by the runner UI. Scenarios use small, real-time delays (the
 * app has no virtual test clock) so every exercise finishes within a fraction of a second.
 */
object ExerciseCatalog {

  val exercises: List<PracticeExercise> = listOf(
    PracticeExercise(
      id = "map-parallel",
      number = "01",
      title = "mapParallel",
      category = ExerciseCategory.IMPLEMENTATION,
      concept = "Structured concurrency, Semaphore, order-preserving awaitAll.",
      sourceHint = "implementation/MapParallel.kt",
      runSkeleton = {
        val result = listOf(1, 2, 3, 4, 5).mapParallel(concurrency = 2) { delay(20); it * 2 }
        verifyEquals(listOf(2, 4, 6, 8, 10), result)
        "mapParallel([1..5], concurrency = 2) = $result"
      },
      runSolution = {
        val result = listOf(1, 2, 3, 4, 5).mapParallelSolution(concurrency = 2) { delay(20); it * 2 }
        verifyEquals(listOf(2, 4, 6, 8, 10), result)
        "mapParallel([1..5], concurrency = 2) = $result"
      },
    ),
    PracticeExercise(
      id = "retry-with-backoff",
      number = "02",
      title = "retryWithBackoff",
      category = ExerciseCategory.IMPLEMENTATION,
      concept = "Retry loop with exponential backoff; rethrow CancellationException.",
      sourceHint = "implementation/RetryWithBackoff.kt",
      runSkeleton = {
        var attempts = 0
        val result = retryWithBackoff(maxAttempts = 3, initialDelayMs = 50, factor = 2.0) {
          attempts++
          if (attempts < 3) error("transient failure $attempts") else "ok"
        }
        verifyEquals("ok", result)
        "Succeeded with \"$result\" on attempt $attempts (after 2 retries)"
      },
      runSolution = {
        var attempts = 0
        val result = retryWithBackoffSolution(maxAttempts = 3, initialDelayMs = 50, factor = 2.0) {
          attempts++
          if (attempts < 3) error("transient failure $attempts") else "ok"
        }
        verifyEquals("ok", result)
        "Succeeded with \"$result\" on attempt $attempts (after 2 retries)"
      },
    ),
    PracticeExercise(
      id = "throttle-first",
      number = "03",
      title = "Flow.throttleFirst",
      category = ExerciseCategory.IMPLEMENTATION,
      concept = "Custom Flow operator via channelFlow + a windowed gate.",
      sourceHint = "implementation/ThrottleFirst.kt",
      runSkeleton = {
        val result = flow {
          emit(1); delay(30); emit(2); delay(80); emit(3)
        }.throttleFirst(windowMs = 50).toList()
        verifyEquals(listOf(1, 3), result)
        "throttleFirst(50ms) kept $result out of [1, 2, 3]"
      },
      runSolution = {
        val result = flow {
          emit(1); delay(30); emit(2); delay(80); emit(3)
        }.throttleFirstSolution(windowMs = 50).toList()
        verifyEquals(listOf(1, 3), result)
        "throttleFirst(50ms) kept $result out of [1, 2, 3]"
      },
    ),
    PracticeExercise(
      id = "cache-then-network",
      number = "04",
      title = "cacheThenNetwork",
      category = ExerciseCategory.IMPLEMENTATION,
      concept = "Emit cache then network; fall back to cache on network error.",
      sourceHint = "implementation/CacheThenNetwork.kt",
      runSkeleton = {
        val result = cacheThenNetwork(
          cache = { delay(10); "cached" },
          network = { delay(20); "fresh" },
        ).toList()
        verifyEquals(listOf("cached", "fresh"), result)
        "cacheThenNetwork emitted $result"
      },
      runSolution = {
        val result = cacheThenNetworkSolution(
          cache = { delay(10); "cached" },
          network = { delay(20); "fresh" },
        ).toList()
        verifyEquals(listOf("cached", "fresh"), result)
        "cacheThenNetwork emitted $result"
      },
    ),
    PracticeExercise(
      id = "chunked",
      number = "05",
      title = "Flow.chunked",
      category = ExerciseCategory.IMPLEMENTATION,
      concept = "Batch by size OR time; channelFlow + Mutex + timer.",
      sourceHint = "implementation/Chunked.kt",
      runSkeleton = {
        val result = flow { emit(1); emit(2); emit(3); emit(4); emit(5) }
          .chunked(maxSize = 2, maxDelayMs = 1_000).toList()
        verifyEquals(listOf(listOf(1, 2), listOf(3, 4), listOf(5)), result)
        "chunked(maxSize = 2) = $result"
      },
      runSolution = {
        val result = flow { emit(1); emit(2); emit(3); emit(4); emit(5) }
          .chunkedSolution(maxSize = 2, maxDelayMs = 1_000).toList()
        verifyEquals(listOf(listOf(1, 2), listOf(3, 4), listOf(5)), result)
        "chunked(maxSize = 2) = $result"
      },
    ),
    PracticeExercise(
      id = "with-timeout-or-fallback",
      number = "06",
      title = "withTimeoutOrFallback",
      category = ExerciseCategory.IMPLEMENTATION,
      concept = "withTimeout + cancel slow work; fallback vs real errors.",
      sourceHint = "implementation/WithTimeoutOrFallback.kt",
      runSkeleton = {
        val result = withTimeoutOrFallback(timeoutMs = 50, fallback = "fallback") {
          delay(200); "done"
        }
        verifyEquals("fallback", result)
        "Slow block exceeded 50ms timeout -> returned \"$result\""
      },
      runSolution = {
        val result = withTimeoutOrFallbackSolution(timeoutMs = 50, fallback = "fallback") {
          delay(200); "done"
        }
        verifyEquals("fallback", result)
        "Slow block exceeded 50ms timeout -> returned \"$result\""
      },
    ),
    PracticeExercise(
      id = "compute-total",
      number = "07",
      title = "computeTotal",
      category = ExerciseCategory.IMPLEMENTATION,
      concept = "combine vs zip: re-emit on either source change.",
      sourceHint = "implementation/ComputeTotal.kt",
      runSkeleton = {
        val quantity = flow { emit(1); delay(40); emit(2) }
        val price = flow { delay(20); emit(10.0); delay(40); emit(20.0) }
        val result = computeTotal(quantity, price).toList()
        verifyEquals(listOf(10.0, 20.0, 40.0), result)
        "computeTotal re-emitted $result on every change"
      },
      runSolution = {
        val quantity = flow { emit(1); delay(40); emit(2) }
        val price = flow { delay(20); emit(10.0); delay(40); emit(20.0) }
        val result = computeTotalSolution(quantity, price).toList()
        verifyEquals(listOf(10.0, 20.0, 40.0), result)
        "computeTotal re-emitted $result on every change"
      },
    ),
    PracticeExercise(
      id = "race-first-successful",
      number = "08",
      title = "raceFirstSuccessful",
      category = ExerciseCategory.IMPLEMENTATION,
      concept = "Race concurrent blocks; first success wins, cancel losers.",
      sourceHint = "implementation/RaceFirstSuccessful.kt",
      runSkeleton = {
        val result = raceFirstSuccessful(
          listOf(
            { delay(100); "slow" },
            { delay(20); "fast" },
          ),
        )
        verifyEquals("fast", result)
        "First successful result was \"$result\" (slower block cancelled)"
      },
      runSolution = {
        val result = raceFirstSuccessfulSolution(
          listOf(
            { delay(100); "slow" },
            { delay(20); "fast" },
          ),
        )
        verifyEquals("fast", result)
        "First successful result was \"$result\" (slower block cancelled)"
      },
    ),
    PracticeExercise(
      id = "debug-cancellation",
      number = "D1",
      title = "Swallowed cancellation",
      category = ExerciseCategory.DEBUGGING,
      concept = "catch(Exception) must rethrow CancellationException.",
      sourceHint = "debugging/BuggyDataLoader.kt",
      runSkeleton = {
        val returned = runCancellationScenario { default, load ->
          BuggyDataLoader().loadOrDefault(default, load)
        }
        verifyEquals(null, returned)
        "Cancellation propagated — no value was returned for a cancelled coroutine"
      },
      runSolution = {
        val returned = runCancellationScenario { default, load ->
          DataLoaderFixed().loadOrDefault(default, load)
        }
        verifyEquals(null, returned)
        "Cancellation propagated — no value was returned for a cancelled coroutine"
      },
    ),
    PracticeExercise(
      id = "debug-global-scope",
      number = "D2",
      title = "Work escaping its scope",
      category = ExerciseCategory.DEBUGGING,
      concept = "GlobalScope.launch returns before results exist; use coroutineScope.",
      sourceHint = "debugging/BuggyAggregator.kt",
      runSkeleton = {
        val total = BuggyAggregator().sumAll(
          listOf({ delay(20); 1 }, { delay(30); 2 }, { delay(40); 3 }),
        )
        verifyEquals(6, total)
        "sumAll awaited every task and returned $total"
      },
      runSolution = {
        val total = AggregatorFixed().sumAll(
          listOf({ delay(20); 1 }, { delay(30); 2 }, { delay(40); 3 }),
        )
        verifyEquals(6, total)
        "sumAll awaited every task and returned $total"
      },
    ),
    PracticeExercise(
      id = "debug-zip-vs-combine",
      number = "D3",
      title = "zip instead of combine",
      category = ExerciseCategory.DEBUGGING,
      concept = "zip pairs lock-step; combine re-emits on either change.",
      sourceHint = "debugging/BuggyPriceCalculator.kt",
      runSkeleton = {
        val quantity = flow { emit(2); delay(40); emit(3) }
        val price = flow { delay(20); emit(10.0); delay(40); emit(20.0) }
        val result = BuggyPriceCalculator().totals(quantity, price).toList()
        verifyEquals(listOf(20.0, 30.0, 60.0), result)
        "totals re-emitted $result on every change"
      },
      runSolution = {
        val quantity = flow { emit(2); delay(40); emit(3) }
        val price = flow { delay(20); emit(10.0); delay(40); emit(20.0) }
        val result = PriceCalculatorFixed().totals(quantity, price).toList()
        verifyEquals(listOf(20.0, 30.0, 60.0), result)
        "totals re-emitted $result on every change"
      },
    ),
  )
}
