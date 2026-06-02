package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

/**
 * Verifies the reference solution for EXERCISE 04. Mirrors
 * [com.huhx0015.androidplayground.practice.implementation.CacheThenNetworkTest].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CacheThenNetworkSolutionTest {

  @Test
  fun emitsCacheValueThenNetworkValue() = runTest {
    val result = cacheThenNetworkSolution(
      cache = { "cached" },
      network = { "fresh" },
    ).toList()

    assertEquals(listOf("cached", "fresh"), result)
  }

  @Test
  fun whenNetworkFails_fallsBackToCacheOnly() = runTest {
    val result = cacheThenNetworkSolution(
      cache = { "cached" },
      network = { throw IOException("network down") },
    ).toList()

    assertEquals(listOf("cached"), result)
  }

  @Test
  fun emitsCacheBeforeTouchingNetwork() = runTest {
    val callOrder = mutableListOf<String>()

    cacheThenNetworkSolution(
      cache = { callOrder.add("cache"); "cached" },
      network = { callOrder.add("network"); "fresh" },
    ).toList()

    assertEquals(listOf("cache", "network"), callOrder)
  }
}
