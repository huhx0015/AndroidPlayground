package com.huhx0015.androidplayground.feature.kotlin.coroutine

import com.huhx0015.androidplayground.core.architecture.BaseIntent

sealed interface CoroutineIntent : BaseIntent {
  data class SelectSection(val section: CoroutineDemoSection) : CoroutineIntent

  data object ClearLog : CoroutineIntent

  data object RunSampleWork : CoroutineIntent

  data object RunDispatcherDemo : CoroutineIntent

  data object RunAsyncParallelDemo : CoroutineIntent
  data object RunAsyncSequentialDemo : CoroutineIntent

  data object StartLongRunningWork : CoroutineIntent
  data object CancelLongRunningWork : CoroutineIntent

  data object RunTryCatchExceptionDemo : CoroutineIntent
  data object RunCoroutineExceptionHandlerDemo : CoroutineIntent

  data object RunColdFlowTwoCollectorsDemo : CoroutineIntent
  data object RunSharedFlowTwoCollectorsDemo : CoroutineIntent

  data object RunChannelBackpressureDemo : CoroutineIntent

  data object RunCoroutineScopeFailureDemo : CoroutineIntent
  data object RunSupervisorScopeFailureDemo : CoroutineIntent

  data object RunNonCancellableCleanupDemo : CoroutineIntent
}
