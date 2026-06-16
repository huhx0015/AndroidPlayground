package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.flow.Flow

/**
 * EXERCISE 1 — Custom Flow operator: throttleFirst.
 *
 * Implement a "throttle first" (a.k.a. "sample first" / leading-edge throttle) operator.
 *
 * Behaviour contract:
 *  - Emit the FIRST item immediately.
 *  - After emitting an item, ignore (drop) any items that arrive within the next [windowMillis].
 *  - Once the window elapses, the next item that arrives is emitted, and a new window starts.
 *  - The window is measured from the moment of the LAST EMITTED item, not from a fixed clock grid.
 *  - Upstream completion and exceptions must propagate normally.
 *
 * Example (windowMillis = 100):
 *   upstream:  A@0ms   B@50ms   C@110ms   D@140ms   E@250ms
 *   emitted:   A       (drop)   C         (drop)    E
 *
 * Notes / things the interviewer is looking for:
 *  - Use the `flow { ... }` builder + `collect`, and rely on a virtual-time-friendly clock.
 *  - Do NOT block the thread; use the coroutine time source so `runTest` virtual time works.
 *    (Hint: `kotlinx.coroutines.currentCoroutineContext()` + a TimeSource, OR track elapsed
 *     virtual time via the test scheduler. A common idiom is comparing timestamps obtained from
 *     a monotonic source; in tests with virtual time you can use the delay-aware clock.)
 *
 * @receiver the upstream flow
 * @param windowMillis the throttle window in milliseconds (must be >= 0)
 * @return a flow applying leading-edge throttling
 */
fun <T> Flow<T>.throttleFirst(windowMillis: Long): Flow<T> {
  TODO("Implement throttleFirst — see the KDoc contract above")
}
