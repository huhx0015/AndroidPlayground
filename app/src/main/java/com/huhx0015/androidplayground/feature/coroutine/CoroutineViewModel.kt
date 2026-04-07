package com.huhx0015.androidplayground.feature.coroutine

import com.huhx0015.androidplayground.core.architecture.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class CoroutineViewModel : BaseViewModel<CoroutineState, CoroutineIntent, CoroutineEvent>() {

  private val _state = MutableStateFlow(CoroutineState())
  override val state: StateFlow<CoroutineState> = _state.asStateFlow()

  private val eventChannel = Channel<CoroutineEvent>(Channel.BUFFERED)
  override val events: Flow<CoroutineEvent> = eventChannel.receiveAsFlow()

  override suspend fun processIntent(intent: CoroutineIntent) {
    when (intent) {
      CoroutineIntent.RunSampleWork -> {
        _state.update { it.copy(isLoading = true, statusText = "Working…") }
        delay(1_500L)
        _state.update { it.copy(isLoading = false, statusText = "Done") }
        eventChannel.send(CoroutineEvent.ShowMessage("Sample coroutine finished"))
      }
    }
  }
}
