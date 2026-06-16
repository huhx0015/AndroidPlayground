# Kotlin Flows / Coroutines Interview Practice

A self-contained, pure-JVM module that mirrors a **1h10m Functional Test** interview:

1. **Implementation challenge** — implement a function from its contract; provided unit tests verify it.
2. **Debugging challenge** — a function whose test is failing; fix the bug **without editing the test**.

Everything here runs as plain JUnit + `kotlinx-coroutines-test` — no Android, no emulator.

---

## Layout

```
practice/
├── src/main/java/com/huhx0015/androidplayground/practice/
│   ├── implementation/     ← Part 1: stubs to implement (TODO)
│   │   ├── FlowOperators.kt        throttleFirst
│   │   ├── RetryWithBackoff.kt     retryWithBackoff
│   │   ├── ParallelMap.kt          parallelMap
│   │   ├── FirstSuccessful.kt      firstSuccessful
│   │   └── ResultFlow.kt           asResult
│   └── debugging/          ← Part 2: bugs to fix
│       ├── StateHolder.kt          StateFlow not emitting
│       ├── ParallelAggregator.kt   swallowed failure / no fail-fast
│       └── EventBuffer.kt          dropped emissions
├── src/test/java/...                ← the verifying tests (Part 2 tests are LOCKED)
└── SOLUTIONS.md                     ← answer key — open ONLY after attempting
```

---

## How to use it (rehearse like the real thing)

**Suggested split for a 70-minute timer**

| Block            | Time   | What                                                        |
|------------------|--------|------------------------------------------------------------|
| Part 1           | ~35 min| Pick 2–3 implementation exercises; make their tests green.  |
| Part 2           | ~20 min| Fix the failing debugging tests without touching them.     |
| Review           | ~15 min| Re-read your code, narrate trade-offs, check against `SOLUTIONS.md`. |

**Workflow per exercise**

1. Read the KDoc contract in the source file.
2. Implement / fix — talk through your reasoning out loud (the rubric rewards communication).
3. Run the test (commands below). Iterate until green.
4. Only then open `SOLUTIONS.md` to compare approaches.

> Commit as you go (`git add -p && git commit`) — the real interview requires all changes committed.

---

## Running the tests

Run everything:
```bash
./gradlew :practice:test
```

Run one challenge (by test class name, wildcard ok):
```bash
./gradlew :practice:test --tests "*ThrottleFirstTest"
./gradlew :practice:test --tests "*ParallelMapTest"
./gradlew :practice:test --tests "*StateHolderTest"
```

See the HTML report after a run:
```
practice/build/reports/tests/test/index.html
```

**Starting state:** every test here FAILS on purpose — Part 1 stubs throw `TODO()`, Part 2 functions contain bugs. Making them all green is the exercise.

---

## What the interviewers look for (keep these in mind)

- Clear problem understanding & structured thinking — restate the contract before coding.
- Clean, idiomatic Kotlin — prefer existing Flow operators over hand-rolled loops.
- Proper Flows + **structured concurrency** — `coroutineScope`/`async`/`awaitAll`, no leaked coroutines.
- Thoughtful error handling & edge cases — empty input, cancellation, fail-fast vs. isolate.
- Strong debugging — form a hypothesis, confirm with the test, fix the root cause (not the symptom).
- Clear communication — narrate your reasoning the whole way.

---

## Tips for the debugging challenges

- Read the **test** first — it is the precise spec.
- Reproduce, then localize: which assertion fails, and what does that imply?
- Common coroutine/Flow traps to scan for:
  - Emitting a **mutable reference** into `StateFlow`/`MutableStateFlow` (no new value ⇒ no emission, and old snapshots mutate underneath you).
  - `supervisorScope` vs `coroutineScope` when you actually want **fail-fast**.
  - `catch`/`try` blocks that **swallow** exceptions (including `CancellationException`).
  - Backpressure operators (`conflate`, `buffer`, `debounce`, `sample`) that **drop** items.
  - `launch` where you needed `withContext`/`await` (returning before the work is done).

## Cleanup

This module is fully isolated. To remove it after the interview:
```bash
git rm -r practice
# then delete the `include(":practice")` line in settings.gradle.kts
```
