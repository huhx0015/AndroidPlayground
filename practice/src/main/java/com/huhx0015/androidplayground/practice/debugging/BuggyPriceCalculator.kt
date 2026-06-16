package com.huhx0015.androidplayground.practice.debugging

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

/**
 * DEBUGGING EXERCISE D3 — Wrong stream-combining operator.
 *
 * The test [com.huhx0015.androidplayground.practice.debugging.BuggyPriceCalculatorTest] is failing.
 * Find and fix the bug in [totals] WITHOUT modifying the test.
 *
 * Symptom: [totals] should re-emit the latest `quantity * unitPrice` whenever EITHER input changes,
 * but it only emits a value for each lock-step pair and stops at the shorter flow.
 *
 * (Reference fix + explanation: see solutions/PriceCalculatorFixed.kt — but try it yourself first.)
 */
class BuggyPriceCalculator {

  /** Emits the latest quantity * unit price whenever either input changes. */
  fun totals(quantity: Flow<Int>, unitPrice: Flow<Double>): Flow<Double> =
    quantity.zip(unitPrice) { q, p -> q * p }
}
