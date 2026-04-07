package com.huhx0015.androidplayground.feature.coroutine

import com.huhx0015.androidplayground.core.architecture.BaseState

data class CoroutineState(
  val isLoading: Boolean = false,
  val statusText: String = "Idle",
) : BaseState
