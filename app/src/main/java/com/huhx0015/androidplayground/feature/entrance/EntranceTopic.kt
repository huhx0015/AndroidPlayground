package com.huhx0015.androidplayground.feature.entrance

sealed class EntranceTopic {
  abstract val title: String

  data object Coroutines : EntranceTopic() {
    override val title = "Coroutines"
  }

  data object KotlinSample : EntranceTopic() {
    override val title = "Kotlin sample"
  }

  data object RecyclerView : EntranceTopic() {
    override val title: String = "RecyclerView"

  }
}
