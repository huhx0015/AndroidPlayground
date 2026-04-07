package com.huhx0015.androidplayground.feature.coroutine.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.huhx0015.androidplayground.feature.coroutine.CoroutineEvent
import com.huhx0015.androidplayground.feature.coroutine.CoroutineIntent
import com.huhx0015.androidplayground.feature.coroutine.CoroutineState
import com.huhx0015.androidplayground.ui.theme.AndroidPlaygroundTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun CoroutineScreen(
  stateFlow: StateFlow<CoroutineState>,
  onIntent: (CoroutineIntent) -> Unit,
  events: Flow<CoroutineEvent>,
) {
  val state by stateFlow.collectAsStateWithLifecycle()
  val snackbarHostState = remember { SnackbarHostState() }
  val lifecycleOwner = LocalLifecycleOwner.current

  LaunchedEffect(lifecycleOwner.lifecycle, events) {
    lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
      events.collect { event ->
        when (event) {
          is CoroutineEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
        }
      }
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    snackbarHost = { SnackbarHost(snackbarHostState) },
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .padding(innerPadding)
        .padding(16.dp),
    ) {
      Text(text = state.statusText)
      Button(
        onClick = { onIntent(CoroutineIntent.RunSampleWork) },
        enabled = !state.isLoading,
        modifier = Modifier.padding(top = 16.dp),
      ) {
        Text(if (state.isLoading) "Running…" else "Run sample coroutine")
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun CoroutineScreenPreview() {
  AndroidPlaygroundTheme {
    CoroutineScreen(
      stateFlow = MutableStateFlow(CoroutineState(statusText = "Idle")),
      onIntent = {},
      events = emptyFlow(),
    )
  }
}
