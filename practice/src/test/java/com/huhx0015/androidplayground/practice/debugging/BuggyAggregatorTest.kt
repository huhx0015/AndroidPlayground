package com.huhx0015.androidplayground.practice.debugging

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

/**
 * DEBUGGING EXERCISE D2 test. Remove @Ignore to reproduce the failure, then fix [BuggyAggregator].
 */
@Ignore("Remove this annotation to work DEBUGGING EXERCISE D2")
@OptIn(ExperimentalCoroutinesApi::class)
class BuggyAggregatorTest {

  @Test
  fun sumsAllTaskResults() = runTest {
    val total = BuggyAggregator().sumAll(
      listOf(
        { delay(10); 1 },
        { delay(20); 2 },
        { delay(30); 3 },
      ),
    )

    assertEquals(6, total)
  }

  @Test
  fun emptyTaskList_returnsZero() = runTest {
    val total = BuggyAggregator().sumAll(emptyList())

    assertEquals(0, total)
  }
}
