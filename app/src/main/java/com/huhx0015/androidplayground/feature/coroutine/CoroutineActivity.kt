package com.huhx0015.androidplayground.feature.coroutine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.huhx0015.androidplayground.feature.coroutine.compose.CoroutineScreen
import com.huhx0015.androidplayground.ui.theme.AndroidPlaygroundTheme

class CoroutineActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()

    setContent {
      AndroidPlaygroundTheme {
        val viewModel: CoroutineViewModel = viewModel()
        CoroutineScreen(
          stateFlow = viewModel.state,
          onIntent = viewModel::sendIntent,
          events = viewModel.events,
        )
      }
    }
  }
}
