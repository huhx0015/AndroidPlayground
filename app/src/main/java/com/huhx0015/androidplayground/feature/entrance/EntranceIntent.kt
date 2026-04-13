package com.huhx0015.androidplayground.feature.entrance

import com.huhx0015.androidplayground.core.architecture.BaseIntent

sealed interface EntranceIntent : BaseIntent {
  data class SelectTab(val tab: EntranceDestinations) : EntranceIntent
  data class OpenTopic(val topic: EntranceTopic) : EntranceIntent
}
