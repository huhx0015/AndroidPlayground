package com.huhx0015.androidplayground.practice.implementation

/**
 * EXERCISE 4 — Structured concurrency + error handling: firstSuccessful.
 *
 * Run all [tasks] concurrently and return the result of the FIRST task to complete
 * SUCCESSFULLY, then cancel the rest.
 *
 * Behaviour contract:
 *  - All tasks start concurrently.
 *  - The function returns as soon as ANY task returns normally; the value returned is
 *    that of the first SUCCESSFUL task (the fastest to succeed).
 *  - A task that FAILS does NOT terminate the race — the remaining tasks keep going.
 *  - Once a winner is found, the still-running (losing) tasks must be cancelled.
 *  - If ALL tasks fail, throw an exception (rethrow the last failure is acceptable).
 *  - An empty [tasks] list is an illegal argument.
 *
 * Hints:
 *  - `coroutineScope { ... }` for structured concurrency.
 *  - A `CompletableDeferred<T>` is a convenient "first one wins" rendezvous.
 *  - `coroutineContext.cancelChildren()` cancels the losers once you have a winner.
 */
suspend fun <T> firstSuccessful(tasks: List<suspend () -> T>): T {
  TODO("Implement firstSuccessful — see the KDoc contract above")
}
