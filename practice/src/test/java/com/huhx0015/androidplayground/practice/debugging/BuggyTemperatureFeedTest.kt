package com.huhx0015.androidplayground.practice.debugging

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

/**
 * DEBUGGING EXERCISE D6 test. Remove @Ignore to reproduce the failure, then fix
 * [BuggyTemperatureFeed].
 */
@Ignore("Remove this annotation to work DEBUGGING EXERCISE D6")
@OptIn(ExperimentalCoroutinesApi::class)
class BuggyTemperatureFeedTest {

  @Test
  fun emitsAllReadingsInOrder() = runTest {
    val result = BuggyTemperatureFeed().readings().toList()

    assertEquals(listOf(18, 20, 22), result)
  }
}
