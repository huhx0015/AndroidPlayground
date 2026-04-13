package com.huhx0015.androidplayground.feature.kotlin.coroutine

import com.huhx0015.androidplayground.core.architecture.BaseState

data class CoroutineState(
  val selectedSection: CoroutineDemoSection = CoroutineDemoSection.Sample,
  val isLoading: Boolean = false,
  val statusText: String = "Idle",
  val logLines: List<String> = emptyList(),
  val cancellationProgress: Int = 0,
  val cancellationRunning: Boolean = false,
) : BaseState
