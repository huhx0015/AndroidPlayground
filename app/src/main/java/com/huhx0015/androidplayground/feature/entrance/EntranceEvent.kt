package com.huhx0015.androidplayground.feature.entrance

import androidx.activity.ComponentActivity
import com.huhx0015.androidplayground.core.architecture.BaseEvent
import kotlin.reflect.KClass

sealed interface EntranceEvent : BaseEvent {
  data class StartActivity(val target: KClass<out ComponentActivity>) : EntranceEvent
}
