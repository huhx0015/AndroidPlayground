package com.huhx0015.androidplayground.practice.runner.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.huhx0015.androidplayground.practice.runner.ExerciseCategory
import com.huhx0015.androidplayground.practice.runner.ExerciseResult
import com.huhx0015.androidplayground.practice.runner.ExerciseStatus
import com.huhx0015.androidplayground.practice.runner.ExercisesRunnerState
import com.huhx0015.androidplayground.practice.runner.ExercisesRunnerViewModel
import com.huhx0015.androidplayground.practice.runner.PracticeExercise
import com.huhx0015.androidplayground.ui.theme.AndroidPlaygroundTheme

@Composable
fun ExercisesRunnerScreen(
  viewModel: ExercisesRunnerViewModel,
  onBackClick: () -> Unit,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  ExercisesRunnerContent(
    state = state,
    exercises = viewModel.exercises,
    onBackClick = onBackClick,
    onToggleSolutions = viewModel::setUseSolutions,
    onRun = viewModel::run,
    onRunAll = viewModel::runAll,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExercisesRunnerContent(
  state: ExercisesRunnerState,
  exercises: List<PracticeExercise>,
  onBackClick: () -> Unit,
  onToggleSolutions: (Boolean) -> Unit,
  onRun: (String) -> Unit,
  onRunAll: () -> Unit,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Functional Test Practice") },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
      )
    },
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      item { RunnerControls(state = state, onToggleSolutions = onToggleSolutions, onRunAll = onRunAll) }

      ExerciseCategory.entries.forEach { category ->
        val categoryExercises = exercises.filter { it.category == category }
        if (categoryExercises.isNotEmpty()) {
          item(key = "header-${category.name}") { SectionHeader(category.label) }
          items(categoryExercises, key = { it.id }) { exercise ->
            ExerciseCard(
              exercise = exercise,
              result = state.resultFor(exercise.id),
              onRun = { onRun(exercise.id) },
            )
          }
        }
      }
    }
  }
}

@Composable
private fun RunnerControls(
  state: ExercisesRunnerState,
  onToggleSolutions: (Boolean) -> Unit,
  onRunAll: () -> Unit,
) {
  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Text(
      text = "Tap Run to execute each exercise live. Failures show what was expected vs. actual.",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Switch(checked = state.useSolutions, onCheckedChange = onToggleSolutions)
        Text(
          text = if (state.useSolutions) "Reference solutions" else "Your code",
          modifier = Modifier.padding(start = 8.dp),
          style = MaterialTheme.typography.labelLarge,
        )
      }
      Button(onClick = onRunAll) { Text("Run all") }
    }
  }
}

@Composable
private fun SectionHeader(label: String) {
  Text(
    text = label,
    style = MaterialTheme.typography.titleMedium,
    fontWeight = FontWeight.Bold,
    modifier = Modifier.padding(top = 8.dp),
  )
}

@Composable
private fun ExerciseCard(
  exercise: PracticeExercise,
  result: ExerciseResult,
  onRun: () -> Unit,
) {
  Card(modifier = Modifier.fillMaxWidth()) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "${exercise.number}  ${exercise.title}",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.SemiBold,
        )
        StatusBadge(result.status)
      }

      Text(
        text = exercise.concept,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )

      if (result.detail.isNotEmpty()) {
        Text(
          text = result.detail,
          style = MaterialTheme.typography.bodySmall,
          fontFamily = FontFamily.Monospace,
          color = detailColor(result.status),
        )
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
      ) {
        OutlinedButton(
          onClick = onRun,
          enabled = result.status != ExerciseStatus.RUNNING,
        ) {
          Text("Run")
        }
      }
    }
  }
}

@Composable
private fun StatusBadge(status: ExerciseStatus) {
  if (status == ExerciseStatus.RUNNING) {
    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
    return
  }

  val (label, color) = when (status) {
    ExerciseStatus.NOT_RUN -> "Not run" to MaterialTheme.colorScheme.surfaceVariant
    ExerciseStatus.PASSED -> "Passed" to PassGreen
    ExerciseStatus.FAILED -> "Failed" to MaterialTheme.colorScheme.errorContainer
    ExerciseStatus.NOT_IMPLEMENTED -> "TODO" to MaterialTheme.colorScheme.tertiaryContainer
    ExerciseStatus.RUNNING -> "" to MaterialTheme.colorScheme.surfaceVariant
  }

  Surface(color = color, shape = MaterialTheme.shapes.small) {
    Text(
      text = label,
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
      style = MaterialTheme.typography.labelMedium,
    )
  }
}

@Composable
private fun detailColor(status: ExerciseStatus): Color = when (status) {
  ExerciseStatus.FAILED -> MaterialTheme.colorScheme.error
  else -> MaterialTheme.colorScheme.onSurface
}

private val PassGreen = Color(0xFFB7E1B0)

@Preview(showBackground = true)
@Composable
private fun ExercisesRunnerContentPreview() {
  AndroidPlaygroundTheme {
    val exercises = listOf(
      PracticeExercise(
        id = "map-parallel",
        number = "01",
        title = "mapParallel",
        category = ExerciseCategory.IMPLEMENTATION,
        concept = "Structured concurrency, Semaphore, order-preserving awaitAll.",
        sourceHint = "implementation/MapParallel.kt",
        runSkeleton = { "" },
        runSolution = { "" },
      ),
      PracticeExercise(
        id = "debug-zip-vs-combine",
        number = "D3",
        title = "zip instead of combine",
        category = ExerciseCategory.DEBUGGING,
        concept = "zip pairs lock-step; combine re-emits on either change.",
        sourceHint = "debugging/BuggyPriceCalculator.kt",
        runSkeleton = { "" },
        runSolution = { "" },
      ),
    )
    ExercisesRunnerContent(
      state = ExercisesRunnerState(
        results = mapOf(
          "map-parallel" to ExerciseResult(ExerciseStatus.PASSED, "mapParallel([1..5], concurrency = 2) = [2, 4, 6, 8, 10]"),
          "debug-zip-vs-combine" to ExerciseResult(ExerciseStatus.FAILED, "expected [20.0, 30.0, 60.0] but was [20.0, 60.0]"),
        ),
      ),
      exercises = exercises,
      onBackClick = {},
      onToggleSolutions = {},
      onRun = {},
      onRunAll = {},
    )
  }
}
