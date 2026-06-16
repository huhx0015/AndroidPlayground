package com.huhx0015.androidplayground.practice.debugging.solutions

import kotlin.coroutines.cancellation.CancellationException

/**
 * Reference fix for DEBUGGING EXERCISE D1.
 *
 * ROOT CAUSE: `catch (error: Exception)` also catches CancellationException (it is an Exception).
 * Coroutine cancellation works by throwing CancellationException at suspension points; swallowing it
 * and returning [default] makes a cancelled coroutine look like it completed normally, breaking
 * structured concurrency.
 *
 * FIX: catch CancellationException first and rethrow it, so only genuine failures fall back to
 * [default].
 */
class DataLoaderFixed {

  suspend fun loadOrDefault(default: String, load: suspend () -> String): String {
    return try {
      load()
    } catch (cancellation: CancellationException) {
      throw cancellation
    } catch (error: Exception) {
      default
    }
  }
}
