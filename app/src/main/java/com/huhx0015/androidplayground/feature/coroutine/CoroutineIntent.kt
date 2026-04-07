package com.huhx0015.androidplayground.feature.coroutine

import com.huhx0015.androidplayground.core.architecture.BaseIntent

sealed interface CoroutineIntent : BaseIntent {
  data object RunSampleWork : CoroutineIntent
}
