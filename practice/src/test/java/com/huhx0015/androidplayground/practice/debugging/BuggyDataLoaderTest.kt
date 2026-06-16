package com.huhx0015.androidplayground.practice.debugging

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Ignore
import org.junit.Test
import java.io.IOException

/**
 * DEBUGGING EXERCISE D1 test. Remove @Ignore to reproduce the failure, then fix [BuggyDataLoader].
 *
 * [normalFailure_stillReturnsDefault] already passes — the fallback behavior is correct. Only
 * [cancellationMustPropagate_notReturnDefault] fails, because cancellation is being swallowed.
 */
@Ignore("Remove this annotation to work DEBUGGING EXERCISE D1")
@OptIn(ExperimentalCoroutinesApi::class)
class BuggyDataLoaderTest {

  private val loader = BuggyDataLoader()

  @Test
  fun normalFailure_stillReturnsDefault() = runTest {
    val result = loader.loadOrDefault(default = "default") {
      throw IOException("boom")
    }

    assertEquals("default", result)
  }

  @Test
  fun cancellationMustPropagate_notReturnDefault() = runTest {
    var returnedValue: String? = null

    val job = launch {
      returnedValue = loader.loadOrDefault(default = "default") {
        delay(1_000)
        "loaded"
      }
    }

    advanceTimeBy(100) // let the child start and suspend inside load()
    job.cancel()
    job.join()

    // If cancellation is swallowed, the function returns "default" and assigns it here.
    assertNull("cancellation should propagate, so no value is assigned", returnedValue)
  }
}
