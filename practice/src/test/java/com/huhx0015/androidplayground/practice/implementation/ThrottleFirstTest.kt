package com.huhx0015.androidplayground.practice.implementation

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ThrottleFirstTest {

  @Test
  fun `emits first item then drops items within the window`() = runTest {
    // Emission timeline (cumulative virtual time):
    //   A@0   B@50   C@110   D@140   E@250
    val upstream = flow {
      emit("A"); delay(50)   // t=0
      emit("B"); delay(60)   // t=50
      emit("C"); delay(30)   // t=110
      emit("D"); delay(110)  // t=140
      emit("E")              // t=250
    }

    val result = upstream.throttleFirst(windowMillis = 100).toList()

    // A emitted; A's window covers [0,100] -> B dropped.
    // C@110 emitted; window covers [110,210] -> D dropped.
    // E@250 emitted.
    assertEquals(listOf("A", "C", "E"), result)
  }

  @Test
  fun `single item passes through`() = runTest {
    val result = flowOf("only").throttleFirst(windowMillis = 1_000).toList()
    assertEquals(listOf("only"), result)
  }

  @Test
  fun `empty upstream emits nothing`() = runTest {
    val result = flowOf<String>().throttleFirst(windowMillis = 100).toList()
    assertEquals(emptyList<String>(), result)
  }
}
