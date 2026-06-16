package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

/**
 * Tests for EXERCISE 03 ([throttleFirst]). Remove @Ignore to start.
 */
@Ignore("Remove this annotation to begin Exercise 03")
@OptIn(ExperimentalCoroutinesApi::class)
class ThrottleFirstTest {

  @Test
  fun emitsFirstItemThenDropsItemsWithinWindow() = runTest {
    val upstream = flow {
      emit("a")            // t = 0   -> emitted, window [0, 100)
      delay(30); emit("b") // t = 30  -> dropped
      delay(30); emit("c") // t = 60  -> dropped
      delay(30); emit("d") // t = 90  -> dropped
      delay(30); emit("e") // t = 120 -> emitted, window reopened
      delay(30); emit("f") // t = 150 -> dropped
    }

    val result = upstream.throttleFirst(windowMs = 100).toList()

    assertEquals(listOf("a", "e"), result)
  }

  @Test
  fun emitsEveryItemWhenSpacedBeyondWindow() = runTest {
    val upstream = flow {
      emit(1)
      delay(200); emit(2)
      delay(200); emit(3)
    }

    val result = upstream.throttleFirst(windowMs = 100).toList()

    assertEquals(listOf(1, 2, 3), result)
  }
}
