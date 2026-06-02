package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import java.io.IOException

/**
 * Tests for EXERCISE 04 ([cacheThenNetwork]). Remove @Ignore to start.
 */
@Ignore("Remove this annotation to begin Exercise 04")
@OptIn(ExperimentalCoroutinesApi::class)
class CacheThenNetworkTest {

  @Test
  fun emitsCacheValueThenNetworkValue() = runTest {
    val result = cacheThenNetwork(
      cache = { "cached" },
      network = { "fresh" },
    ).toList()

    assertEquals(listOf("cached", "fresh"), result)
  }

  @Test
  fun whenNetworkFails_fallsBackToCacheOnly() = runTest {
    val result = cacheThenNetwork(
      cache = { "cached" },
      network = { throw IOException("network down") },
    ).toList()

    assertEquals(listOf("cached"), result)
  }

  @Test
  fun emitsCacheBeforeTouchingNetwork() = runTest {
    val callOrder = mutableListOf<String>()

    cacheThenNetwork(
      cache = { callOrder.add("cache"); "cached" },
      network = { callOrder.add("network"); "fresh" },
    ).toList()

    assertEquals(listOf("cache", "network"), callOrder)
  }
}
