package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.flow.Flow

/**
 * EXERCISE 07 — Combining two streams (combine vs zip).
 *
 * Implement [computeTotal] to produce the running total `quantity * price` that re-emits whenever
 * EITHER input changes, always using the latest value seen from each.
 *
 * Contract:
 *  - Emit nothing until BOTH flows have emitted at least once.
 *  - After that, every emission from either flow produces a new total using the latest of the other.
 *
 * Hint: this is exactly what kotlinx.coroutines.flow.combine does. zip would pair items lock-step
 * (1st-with-1st, 2nd-with-2nd) and stop at the shorter flow — which is the wrong behavior here.
 *
 * The provided test class is [ComputeTotalTest] (currently @Ignore'd — remove to start).
 */
fun computeTotal(quantity: Flow<Int>, price: Flow<Double>): Flow<Double> {
  TODO("Implement computeTotal — see the KDoc contract above")
}
