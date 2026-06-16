package com.huhx0015.androidplayground.practice.implementation.solutions

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.delay

/**
 * Reference solution for EXERCISE 05.
 *
 * The buffer is shared between the collector and a launched timer coroutine, so every access goes
 * through a [Mutex]. The first item of a batch starts a timer that flushes after [maxDelayMs];
 * reaching [maxSize] flushes immediately and cancels that timer. When upstream finishes we cancel
 * the pending timer and flush whatever is left, so no items are lost.
 */
fun <T> Flow<T>.chunkedSolution(maxSize: Int, maxDelayMs: Long): Flow<List<T>> = channelFlow {
  val mutex = Mutex()
  val buffer = mutableListOf<T>()
  var timerJob: Job? = null

  // Caller must hold [mutex].
  suspend fun flushLocked() {
    if (buffer.isEmpty()) return
    val batch = buffer.toList()
    buffer.clear()
    send(batch)
  }

  collect { value ->
    mutex.withLock {
      buffer.add(value)
      if (buffer.size >= maxSize) {
        timerJob?.cancel()
        timerJob = null
        flushLocked()
      } else if (timerJob == null) {
        timerJob = launch {
          delay(maxDelayMs)
          mutex.withLock {
            flushLocked()
            timerJob = null
          }
        }
      }
    }
  }

  // Upstream is done — stop the timer and emit the remainder.
  timerJob?.cancel()
  mutex.withLock { flushLocked() }
}
