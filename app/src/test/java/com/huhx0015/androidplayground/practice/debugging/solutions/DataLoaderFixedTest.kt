package com.huhx0015.androidplayground.practice.debugging.solutions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.io.IOException

/**
 * Verifies the reference fix for D1. Mirrors
 * [com.huhx0015.androidplayground.practice.debugging.BuggyDataLoaderTest] and passes.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DataLoaderFixedTest {

  private val loader = DataLoaderFixed()

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

    advanceTimeBy(100)
    job.cancel()
    job.join()

    assertNull("cancellation should propagate, so no value is assigned", returnedValue)
  }
}
