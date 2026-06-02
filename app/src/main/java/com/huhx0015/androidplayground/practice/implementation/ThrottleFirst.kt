package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.flow.Flow

/**
 * EXERCISE 03 — Custom Flow operator: throttleFirst.
 *
 * Implement [throttleFirst] so it emits the first item it sees, then ignores every item that
 * arrives within [windowMs] of that emission. Once the window has elapsed, the next item is
 * emitted and opens a new window.
 *
 * Example with windowMs = 100 and items at t = 0, 30, 60, 90, 120, 150:
 *   emit @0, drop @30/60/90 (inside window), emit @120 (window reopened), drop @150.
 *
 * Contract:
 *  - The very first upstream item is always emitted.
 *  - Items arriving while a window is open are dropped.
 *  - Cancellation of the collector must cancel the upstream collection (don't break structured
 *    concurrency).
 *
 * Hints: channelFlow { }, a boolean "can emit" gate, and a launched coroutine that re-opens the
 * gate after delay(windowMs). Avoid reading wall-clock time — let the virtual clock drive it.
 *
 * The provided test class is [ThrottleFirstTest] (currently @Ignore'd — remove to start).
 */
fun <T> Flow<T>.throttleFirst(windowMs: Long): Flow<T> {
  TODO("Implement throttleFirst — see the KDoc contract above")
}
