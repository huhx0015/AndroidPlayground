package com.huhx0015.androidplayground.practice.runner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

/**
 * Drives the functional-test practice runner. Each exercise is executed in [viewModelScope], its
 * thrown errors are mapped to an [ExerciseStatus], and the latest [ExerciseResult] is published to
 * the UI. Plain ViewModel + StateFlow, matching the newer feature flows in this project.
 */
class ExercisesRunnerViewModel : ViewModel() {

  val exercises: List<PracticeExercise> = ExerciseCatalog.exercises

  private val _state = MutableStateFlow(ExercisesRunnerState())
  val state: StateFlow<ExercisesRunnerState> = _state.asStateFlow()

  fun setUseSolutions(useSolutions: Boolean) {
    _state.update { it.copy(useSolutions = useSolutions) }
  }

  fun run(id: String) {
    val exercise = exercises.firstOrNull { it.id == id } ?: return
    viewModelScope.launch { execute(exercise) }
  }

  fun runAll() {
    // Launch each independently so they run concurrently rather than back-to-back.
    exercises.forEach { exercise -> viewModelScope.launch { execute(exercise) } }
  }

  private suspend fun execute(exercise: PracticeExercise) {
    setResult(exercise.id, ExerciseResult(ExerciseStatus.RUNNING))

    val runnable = if (_state.value.useSolutions) exercise.runSolution else exercise.runSkeleton
    val result = try {
      ExerciseResult(ExerciseStatus.PASSED, runnable())
    } catch (cancellation: CancellationException) {
      throw cancellation
    } catch (notImplemented: NotImplementedError) {
      ExerciseResult(
        ExerciseStatus.NOT_IMPLEMENTED,
        "Not implemented yet — fill in the TODO in ${exercise.sourceHint}",
      )
    } catch (error: Throwable) {
      ExerciseResult(ExerciseStatus.FAILED, error.message ?: error.toString())
    }

    setResult(exercise.id, result)
  }

  private fun setResult(id: String, result: ExerciseResult) {
    _state.update { it.copy(results = it.results + (id to result)) }
  }
}
