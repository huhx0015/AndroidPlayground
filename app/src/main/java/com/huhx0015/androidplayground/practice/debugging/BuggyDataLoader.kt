package com.huhx0015.androidplayground.practice.debugging

/**
 * DEBUGGING EXERCISE D1 — Swallowed cancellation.
 *
 * The test [com.huhx0015.androidplayground.practice.debugging.BuggyDataLoaderTest] is failing.
 * Find and fix the bug in [loadOrDefault] WITHOUT modifying the test.
 *
 * Symptom: when the caller is cancelled while [load] is suspended, the function returns [default]
 * instead of letting the cancellation propagate.
 *
 * (Reference fix + explanation: see solutions/DataLoaderFixed.kt — but try it yourself first.)
 */
class BuggyDataLoader {

  /** Returns the result of [load], or [default] if [load] fails. */
  suspend fun loadOrDefault(default: String, load: suspend () -> String): String {
    return try {
      load()
    } catch (error: Exception) {
      default
    }
  }
}
