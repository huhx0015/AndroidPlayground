package com.huhx0015.androidplayground.practice.debugging.solutions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Reference fix for DEBUGGING EXERCISE D3.
 *
 * ROOT CAUSE: `zip` pairs emissions positionally — 1st-with-1st, 2nd-with-2nd — and completes when
 * the shorter flow ends. It does NOT re-emit when only one source changes, so a price update that
 * arrives without a matching quantity update is never reflected.
 *
 * FIX: use `combine`, which re-emits on every change from either flow using the latest value of the
 * other (after both have emitted at least once).
 */
class PriceCalculatorFixed {

  fun totals(quantity: Flow<Int>, unitPrice: Flow<Double>): Flow<Double> =
    combine(quantity, unitPrice) { q, p -> q * p }
}
