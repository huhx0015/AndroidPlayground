package com.huhx0015.androidplayground.practice.runner

/** Outcome of running a single exercise. */
enum class ExerciseStatus {
  NOT_RUN,
  RUNNING,
  PASSED,
  FAILED,
  NOT_IMPLEMENTED,
}

/** Last result for one exercise: a [status] plus a human-readable [detail] line. */
data class ExerciseResult(
  val status: ExerciseStatus = ExerciseStatus.NOT_RUN,
  val detail: String = "",
)

/**
 * UI state for the runner screen.
 *
 * @param useSolutions when true, the "Run" actions invoke the reference solutions instead of the
 *   skeleton/buggy code you edit.
 * @param results last [ExerciseResult] per exercise id (absent = not run yet).
 */
data class ExercisesRunnerState(
  val useSolutions: Boolean = false,
  val results: Map<String, ExerciseResult> = emptyMap(),
) {
  fun resultFor(id: String): ExerciseResult = results[id] ?: ExerciseResult()
}
