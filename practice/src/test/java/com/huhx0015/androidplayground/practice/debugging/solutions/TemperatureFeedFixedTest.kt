package com.huhx0015.androidplayground.practice.debugging.solutions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Verifies the reference fix for D6. Mirrors
 * [com.huhx0015.androidplayground.practice.debugging.BuggyTemperatureFeedTest] and passes.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TemperatureFeedFixedTest {

  @Test
  fun emitsAllReadingsInOrder() = runTest {
    val result = TemperatureFeedFixed().readings().toList()

    assertEquals(listOf(18, 20, 22), result)
  }
}
