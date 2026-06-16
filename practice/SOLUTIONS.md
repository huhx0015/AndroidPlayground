# Solutions & Rationale

> Attempt every exercise first and run its test. Open this only to compare approaches.

---

## Part 1 — Implementation

### 1. `throttleFirst` (leading-edge throttle)

```kotlin
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> Flow<T>.throttleFirst(windowMillis: Long): Flow<T> = channelFlow {
  var canEmit = true
  collect { value ->
    if (canEmit) {
      send(value)
      canEmit = false
      launch {
        delay(windowMillis)   // open a cooldown window…
        canEmit = true        // …then allow the next item through
      }
    }
    // items arriving while canEmit == false are dropped
  }
}
```

**Why this shape.** The window is driven by `delay`, so it advances with `runTest`'s virtual
clock and the test is deterministic. A tempting alternative — record `System.nanoTime()` at each
emission and compare — uses the *wall clock*, which `runTest` does **not** virtualize: every
emission would look simultaneous and only the first would survive. Drive timing with `delay`, not
wall-clock reads, whenever you want it testable.

**Caveat to mention out loud:** `canEmit` is touched from two coroutines. That's safe here because
the test dispatcher is single-threaded; for a multi-threaded dispatcher you'd guard it (e.g. a
`Mutex`, an atomic, or a timestamp tracked inside the single collector coroutine).

---

### 2. `retryWithBackoff`

```kotlin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen
import kotlin.math.pow

fun <T> Flow<T>.retryWithBackoff(
  maxAttempts: Int,
  initialDelayMillis: Long,
  factor: Double = 2.0,
): Flow<T> = retryWhen { _, attempt ->          // attempt is 0-based
  if (attempt < maxAttempts - 1) {
    val backoff = (initialDelayMillis * factor.pow(attempt.toDouble())).toLong()
    delay(backoff)
    true                                         // retry
  } else {
    false                                        // give up -> original exception rethrown
  }
}
```

**Why `retryWhen` over a manual loop.** It re-subscribes to the upstream for you, exposes the 0-based
`attempt` index, and rethrows the last cause automatically when you return `false`. Backoff before
retry *r* is `initialDelay * factor^r`, so for `(3, 100, 2.0)` the delays are 100ms then 200ms = 300ms
of virtual time before the 3rd attempt succeeds.

Production-worthy extras to talk about: a `maxDelay` cap, jitter to avoid thundering herds, and only
retrying *retryable* exceptions (e.g. `IOException`, not `CancellationException` — `retryWhen` already
won't see cancellation).

---

### 3. `parallelMap`

```kotlin
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun <T, R> List<T>.parallelMap(transform: suspend (T) -> R): List<R> =
  coroutineScope {
    map { item -> async { transform(item) } }.awaitAll()
  }
```

**Why it satisfies every clause.**
- *Concurrent:* every `async` starts before any `await`, so N×100ms tasks finish in ~100ms.
- *Ordered:* `map` builds the `Deferred`s in input order; `awaitAll` preserves it.
- *Fail-fast:* `async` is not a supervisor — one failure cancels the `coroutineScope`, which cancels
  the siblings and rethrows. No leaked coroutines (structured concurrency).
- *Empty:* `emptyList().awaitAll()` is `emptyList()`.

---

### 4. `firstSuccessful`

```kotlin
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.coroutineContext

suspend fun <T> firstSuccessful(tasks: List<suspend () -> T>): T {
  require(tasks.isNotEmpty()) { "tasks must not be empty" }
  return coroutineScope {
    val winner = CompletableDeferred<T>()
    val remaining = AtomicInteger(tasks.size)

    tasks.forEach { task ->
      launch {
        try {
          winner.complete(task())                 // first success wins (idempotent)
        } catch (e: CancellationException) {
          throw e                                  // never swallow cancellation
        } catch (e: Throwable) {
          if (remaining.decrementAndGet() == 0) {
            winner.completeExceptionally(e)        // all failed -> surface last error
          }
        }
      }
    }

    val result = winner.await()
    coroutineContext.cancelChildren()              // cancel the losers
    result
  }
}
```

**Why a `CompletableDeferred`.** It's a one-shot rendezvous: the first task to succeed calls
`complete(...)` (later `complete` calls are no-ops), the body unblocks, then `cancelChildren()` stops
the rest. Failures don't end the race — they just decrement a counter, and only when the counter hits
zero do we fail the whole thing. `CancellationException` is rethrown so structured cancellation keeps
working.

A neat alternative is `select { deferreds.forEach { onAwait {...} } }`, but it makes "ignore failures,
wait for the next" awkward — the deferred approach is clearer.

---

### 5. `asResult`

```kotlin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

fun <T> Flow<T>.asResult(): Flow<Result<T>> =
  map { Result.success(it) }
    .catch { emit(Result.failure(it)) }
```

**Why this is correct & complete.** `map` wraps each value; `catch` only intercepts *upstream*
failures and turns the terminal exception into one final `Result.failure`, after which the flow
completes normally so the collector never throws. `catch` already ignores `CancellationException`, so
cooperative cancellation still propagates. (`kotlin.Result` is `out T`, so `Result.failure(...)`
slots into `Flow<Result<T>>` cleanly.)

---

## Part 2 — Debugging

### 1. `StateHolder` — only one emission

**Root cause.** `_state.value = items` stores the **same** `MutableList` instance every time.
`StateFlow` only emits when the new value is `!=` the previous one — but the previous value *is* that
same object, now mutated, so it always compares equal and downstream sees nothing after the first
change. Worse, snapshots a collector already captured keep mutating because they alias the live list.

**Fix — emit a fresh immutable snapshot:**

```kotlin
fun add(item: String) {
  items.add(item)
  _state.value = items.toList()   // new immutable list each time
}
```

(Or drop the backing `items` field entirely and do
`_state.update { it + item }` with `MutableStateFlow.update`.)

---

### 2. `ParallelAggregator` — returns a partial sum instead of failing

**Root cause.** `supervisorScope` *isolates* child failures, so a throwing `async` does **not**
cancel the scope; the `try/catch` around `await()` then swallows that failure and the function returns
a partial sum. Fail-fast was lost.

**Fix — use `coroutineScope` (and let `awaitAll` rethrow):**

```kotlin
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun aggregate(tasks: List<suspend () -> Int>): Int = coroutineScope {
  tasks.map { task -> async { task() } }
    .awaitAll()
    .sum()
}
```

With `coroutineScope`, one failing `async` cancels the siblings and propagates the exception — exactly
the fail-fast contract. Use `supervisorScope` only when you genuinely want failures isolated.

---

### 3. `EventBuffer` — events get dropped

**Root cause.** `conflate()` keeps only the **latest** value while the slow `map` is busy. The
producer emits 1..5 during the first 100ms processing step, so all of 2,3,4 are conflated away and the
output is `[1, 5]`.

**Fix — remove conflation** (back-pressure the producer):

```kotlin
fun processEvents(events: Flow<Int>): Flow<Int> =
  events.map { event ->
    delay(100)
    event
  }
```

If you need the producer to run ahead *without losing items*, use `.buffer()` (unbounded/explicit
capacity) instead of `.conflate()`. `conflate`/`debounce`/`sample` are *lossy by design* — only reach
for them when dropping intermediate values is actually desired.
```
