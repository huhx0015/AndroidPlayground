package com.huhx0015.androidplayground.feature.entrance

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.huhx0015.androidplayground.feature.entrance.composables.EntranceScreen
import com.huhx0015.androidplayground.ui.theme.AndroidPlaygroundTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()

    setContent {
      AndroidPlaygroundTheme {
        val viewModel: EntranceViewModel = viewModel()
        val lifecycleOwner = LocalLifecycleOwner.current

        LaunchedEffect(viewModel) {
          lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
              when (event) {
                is EntranceEvent.StartActivity ->
                  startActivity(Intent(this@MainActivity, event.target.java))
              }
            }
          }
        }

        EntranceScreen(viewModel)
      }
    }
  }
}
