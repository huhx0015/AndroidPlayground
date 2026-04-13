package com.huhx0015.androidplayground.feature.kotlin.coroutine

enum class CoroutineDemoSection(
  val title: String,
  val interviewPrompt: String,
) {
  Sample(
    title = "Sample",
    interviewPrompt = "Why tie work to viewModelScope instead of GlobalScope?",
  ),
  Dispatchers(
    title = "Dispatchers",
    interviewPrompt = "When do Main vs IO vs Default apply? What is main-safe?",
  ),
  AsyncStructured(
    title = "Async & scope",
    interviewPrompt = "What is structured concurrency? async vs launch?",
  ),
  Cancellation(
    title = "Cancellation",
    interviewPrompt = "What is cooperative cancellation? ensureActive vs blocking?",
  ),
  Exceptions(
    title = "Exceptions",
    interviewPrompt = "How do failures propagate in coroutineScope vs supervisorScope?",
  ),
  FlowSharing(
    title = "Flow & shareIn",
    interviewPrompt = "Cold vs hot Flow — when use shareIn / WhileSubscribed?",
  ),
  Channel(
    title = "Channel",
    interviewPrompt = "Channel vs Flow — when is a bounded Channel appropriate?",
  ),
  SupervisorVsScope(
    title = "Scope vs supervisor",
    interviewPrompt = "If one child fails, what happens to siblings?",
  ),
  NonCancellable(
    title = "NonCancellable",
    interviewPrompt = "When is withContext(NonCancellable) justified?",
  ),
}
