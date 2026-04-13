package com.huhx0015.androidplayground.feature.kotlin.coroutine

import com.huhx0015.androidplayground.core.architecture.BaseEvent

sealed interface CoroutineEvent : BaseEvent {
  data class ShowMessage(val message: String) : CoroutineEvent
}
