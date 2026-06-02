package com.huhx0015.androidplayground.practice.implementation

/**
 * EXERCISE 08 — Race for the first successful result.
 *
 * Implement [raceFirstSuccessful] to run every block in [blocks] concurrently and return the result
 * of the FIRST one that completes successfully, cancelling the rest as soon as a winner is found.
 *
 * Contract:
 *  - Run all blocks concurrently.
 *  - Return the first SUCCESSFUL result; a block that throws does not win — keep waiting on others.
 *  - Once there is a winner, cancel the remaining blocks (don't leave work running).
 *  - If every block fails, throw (the first failure is a reasonable choice).
 *  - An empty list is invalid: throw IllegalArgumentException.
 *
 * Hints: coroutineScope { }, launch one child per block, a CompletableDeferred<T> for the first
 * success, and a thread-safe failure counter. Cancel the children once the deferred resolves.
 * Remember to rethrow CancellationException inside each child.
 *
 * The provided test class is [RaceFirstSuccessfulTest] (currently @Ignore'd — remove to start).
 */
suspend fun <T> raceFirstSuccessful(blocks: List<suspend () -> T>): T {
  TODO("Implement raceFirstSuccessful — see the KDoc contract above")
}
