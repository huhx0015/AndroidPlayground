# Functional-Test Practice Pack

Pure-Kotlin coding katas (no Android UI) for practicing a **live-coding functional round**. The
round has two parts, and this pack mirrors both:

1. **Implementation challenge** — you're given an interface/function description and provided unit
   tests; write the concrete implementation until the tests pass. Drills **Kotlin Flows, structured
   concurrency, async programming, and error handling in concurrent/reactive code**.
2. **Debugging challenge** — a function whose test is failing; find and fix the bug **without
   modifying the test**. Drills **debugging and analytical problem-solving**.

Everything here is plain Kotlin running on `kotlinx-coroutines-test`'s virtual clock — `delay(n)`
advances time instantly, so timing-sensitive tests are deterministic with no real waiting.

## How it's organized

```
practice/
  implementation/                 ← 8 exercises: <Name>.kt with a TODO() skeleton + KDoc contract
    solutions/                    ← reference implementations (<Name>Solution)
  debugging/                      ← 3 exercises: Buggy<Name>.kt with a planted bug
    solutions/                    ← fixed versions (<Name>Fixed) + a comment explaining the root cause
```

Tests live under `app/src/test/.../practice/` in the same structure.

- **Skeleton / buggy tests are `@Ignore`'d by default** so the suite stays green until you choose an
  exercise. To work one: open its test class, **remove the `@Ignore(...)` annotation**, run it, watch
  it fail, then edit the corresponding `implementation/` or `debugging/` source file until it passes.
- **Solution tests are always active.** They prove every reference answer is correct and double as
  a worked example if you get stuck — peek only after attempting.

## Running the tests

```bash
# All practice tests (solution/fixed tests pass; @Ignore'd skeletons are skipped)
./gradlew :app:testDebugUnitTest --tests "com.huhx0015.androidplayground.practice.*"

# A single exercise's tests
./gradlew :app:testDebugUnitTest --tests "com.huhx0015.androidplayground.practice.implementation.MapParallelTest"
```

## Implementation exercises

| #  | Function | Concept drilled | Difficulty |
|----|----------|-----------------|------------|
| 01 | `Iterable.mapParallel(concurrency, transform)` | Structured concurrency, `Semaphore`, order-preserving `awaitAll`, failure propagation | Medium |
| 02 | `retryWithBackoff(maxAttempts, initialDelayMs, factor, block)` | Retry loops, exponential backoff timing, rethrowing `CancellationException` | Medium |
| 03 | `Flow.throttleFirst(windowMs)` | Custom Flow operator, `channelFlow`, time-windowed gating | Medium–Hard |
| 04 | `cacheThenNetwork(cache, network)` | `flow {}`, emit-then-fallback, reactive error handling | Medium |
| 05 | `Flow.chunked(maxSize, maxDelayMs)` | Size+time batching, `channelFlow` + `Mutex` + timer, backpressure | Hard |
| 06 | `withTimeoutOrFallback(timeoutMs, fallback, block)` | `withTimeout`, cancellation of slow work, fallback vs real errors | Easy–Medium |
| 07 | `computeTotal(quantity, price)` | `combine` vs `zip` semantics | Easy |
| 08 | `raceFirstSuccessful(blocks)` | Concurrent racing, first-success wins, cancel losers, error aggregation | Hard |

## Debugging exercises

| #  | File | Planted bug |
|----|------|-------------|
| D1 | `BuggyDataLoader` | `catch (Exception)` swallows `CancellationException`, breaking cancellation |
| D2 | `BuggyAggregator` | Work launched into `GlobalScope` and never awaited → returns before results exist |
| D3 | `BuggyPriceCalculator` | Uses `zip` (lock-step pairing) where `combine` (latest-of-each) is required |

## Interview tips

- **Narrate your reasoning.** State the contract and edge cases (empty input, failures, cancellation)
  before you write code, then talk through your choices as you go.
- **Prefer structured concurrency.** `coroutineScope` / `supervisorScope` over `GlobalScope`; let the
  scope handle cancellation and error propagation.
- **Always let `CancellationException` propagate.** When catching broadly, rethrow it first — swallowing
  it is the single most common reactive bug.
- **Lean on the virtual clock.** Assert timing with `currentTime` / `advanceTimeBy` / `advanceUntilIdle`
  instead of real delays.
- **Think about which operator fits:** `combine` vs `zip`, `flatMapLatest` vs `flatMapMerge`,
  `flatMapConcat`; hot (`StateFlow`/`SharedFlow`) vs cold flows.
