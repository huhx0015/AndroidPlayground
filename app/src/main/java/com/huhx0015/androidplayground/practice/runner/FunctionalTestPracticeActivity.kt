package com.huhx0015.androidplayground.practice.runner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.huhx0015.androidplayground.practice.runner.composables.ExercisesRunnerScreen
import com.huhx0015.androidplayground.ui.theme.AndroidPlaygroundTheme

/**
 * Hosts the functional-test practice runner: a screen that executes each implementation/debugging
 * exercise live and shows pass / fail / TODO, with a toggle between your code and the reference
 * solutions.
 */
class FunctionalTestPracticeActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      AndroidPlaygroundTheme {
        val viewModel: ExercisesRunnerViewModel = viewModel()
        ExercisesRunnerScreen(viewModel = viewModel, onBackClick = ::finish)
      }
    }
  }
}
