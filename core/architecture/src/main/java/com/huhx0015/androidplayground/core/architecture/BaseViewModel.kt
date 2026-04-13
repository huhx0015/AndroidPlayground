package com.huhx0015.androidplayground.core.architecture

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : BaseState, I : BaseIntent, E : BaseEvent>(
  private val coroutineScope: CoroutineScope,
) {
  abstract val state: StateFlow<S>
  abstract val events: Flow<E>

  private val intentChannel = Channel<I>(Channel.UNLIMITED)

  init {
    coroutineScope.launch {
      intentChannel.consumeAsFlow()
        .collect { intent -> processIntent(intent) }
    }
  }

  protected abstract suspend fun processIntent(intent: I)

  fun sendIntent(intent: I) {
    coroutineScope.launch { intentChannel.send(intent) }
  }
}
