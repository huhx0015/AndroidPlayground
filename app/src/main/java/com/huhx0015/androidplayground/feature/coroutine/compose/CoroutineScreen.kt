package com.huhx0015.androidplayground.feature.coroutine.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.huhx0015.androidplayground.feature.coroutine.CoroutineDemoSection
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
  val contentScroll = rememberScrollState()

  LaunchedEffect(state.logLines.size) {
    contentScroll.animateScrollTo(contentScroll.maxValue)
  }

  LaunchedEffect(lifecycleOwner.lifecycle, events) {
    lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
      events.collect { event ->
        when (event) {
          is CoroutineEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
        }
      }
    }
  }

  val sections = CoroutineDemoSection.entries
  val tabIndex = sections.indexOf(state.selectedSection).coerceAtLeast(0)

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    snackbarHost = { SnackbarHost(snackbarHostState) },
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize(),
    ) {
      ScrollableTabRow(selectedTabIndex = tabIndex) {
        sections.forEach { section ->
          Tab(
            selected = state.selectedSection == section,
            onClick = { onIntent(CoroutineIntent.SelectSection(section)) },
            text = { Text(section.title) },
          )
        }
      }

      Column(
        modifier = Modifier
          .weight(1f)
          .verticalScroll(contentScroll)
          .padding(16.dp),
      ) {
        Text(
          text = state.selectedSection.interviewPrompt,
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = state.statusText,
          style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(16.dp))

        DemoSectionActions(section = state.selectedSection, state = state, onIntent = onIntent)

        Spacer(modifier = Modifier.height(24.dp))
        Text(
          text = "Log",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
          onClick = { onIntent(CoroutineIntent.ClearLog) },
          modifier = Modifier.fillMaxWidth(),
        ) {
          Text("Clear log")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = if (state.logLines.isEmpty()) {
            "(empty — run a demo to append timestamped lines)"
          } else {
            state.logLines.joinToString("\n")
          },
          style = MaterialTheme.typography.bodySmall,
          modifier = Modifier.fillMaxWidth(),
        )
      }
    }
  }
}

@Composable
private fun DemoSectionActions(
  section: CoroutineDemoSection,
  state: CoroutineState,
  onIntent: (CoroutineIntent) -> Unit,
) {
  when (section) {
    CoroutineDemoSection.Sample -> {
      Button(
        onClick = { onIntent(CoroutineIntent.RunSampleWork) },
        enabled = !state.isLoading,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text(if (state.isLoading) "Running…" else "Run sample (delay + snackbar)")
      }
    }
    CoroutineDemoSection.Dispatchers -> {
      Button(
        onClick = { onIntent(CoroutineIntent.RunDispatcherDemo) },
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text("Run CPU work on Default (observe thread names in log)")
      }
    }
    CoroutineDemoSection.AsyncStructured -> {
      Button(
        onClick = { onIntent(CoroutineIntent.RunAsyncParallelDemo) },
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text("Parallel async (~800ms total)")
      }
      Spacer(modifier = Modifier.height(8.dp))
      Button(
        onClick = { onIntent(CoroutineIntent.RunAsyncSequentialDemo) },
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text("Sequential delays (~1600ms total)")
      }
    }
    CoroutineDemoSection.Cancellation -> {
      Text(
        text = "Progress: ${state.cancellationProgress}/30",
        style = MaterialTheme.typography.bodySmall,
      )
      Spacer(modifier = Modifier.height(8.dp))
      Button(
        onClick = { onIntent(CoroutineIntent.StartLongRunningWork) },
        enabled = !state.cancellationRunning,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text("Start long work (cancellable)")
      }
      Spacer(modifier = Modifier.height(8.dp))
      Button(
        onClick = { onIntent(CoroutineIntent.CancelLongRunningWork) },
        enabled = state.cancellationRunning,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text("Cancel + NonCancellable cleanup")
      }
    }
    CoroutineDemoSection.Exceptions -> {
      Button(
        onClick = { onIntent(CoroutineIntent.RunTryCatchExceptionDemo) },
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text("Try/catch around failing suspend call")
      }
      Spacer(modifier = Modifier.height(8.dp))
      Button(
        onClick = { onIntent(CoroutineIntent.RunCoroutineExceptionHandlerDemo) },
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text("CoroutineExceptionHandler on scope")
      }
    }
    CoroutineDemoSection.FlowSharing -> {
      Button(
        onClick = { onIntent(CoroutineIntent.RunColdFlowTwoCollectorsDemo) },
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text("Cold flow: two collectors (double upstream)")
      }
      Spacer(modifier = Modifier.height(8.dp))
      Button(
        onClick = { onIntent(CoroutineIntent.RunSharedFlowTwoCollectorsDemo) },
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text("shareIn: staggered subscribers (single upstream)")
      }
    }
    CoroutineDemoSection.Channel -> {
      Button(
        onClick = { onIntent(CoroutineIntent.RunChannelBackpressureDemo) },
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text("Bounded Channel trySend backpressure")
      }
    }
    CoroutineDemoSection.SupervisorVsScope -> {
      Button(
        onClick = { onIntent(CoroutineIntent.RunCoroutineScopeFailureDemo) },
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text("coroutineScope: one child fails → scope fails")
      }
      Spacer(modifier = Modifier.height(8.dp))
      Button(
        onClick = { onIntent(CoroutineIntent.RunSupervisorScopeFailureDemo) },
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text("supervisorScope: sibling can still complete")
      }
    }
    CoroutineDemoSection.NonCancellable -> {
      Button(
        onClick = { onIntent(CoroutineIntent.RunNonCancellableCleanupDemo) },
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text("Self-cancel mid-work + cleanup in finally")
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
