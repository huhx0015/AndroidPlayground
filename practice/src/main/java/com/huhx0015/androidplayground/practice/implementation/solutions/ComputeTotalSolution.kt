package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Reference solution for EXERCISE 07.
 *
 * combine waits until both upstreams have produced a value, then re-emits on every subsequent
 * change from either side using the latest value of the other.
 */
fun computeTotalSolution(quantity: Flow<Int>, price: Flow<Double>): Flow<Double> =
  combine(quantity, price) { latestQuantity, latestPrice -> latestQuantity * latestPrice }
