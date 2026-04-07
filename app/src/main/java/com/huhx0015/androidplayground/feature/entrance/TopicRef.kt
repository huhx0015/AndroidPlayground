package com.huhx0015.androidplayground.feature.entrance

sealed class TopicRef {
  abstract val title: String

  data object AndroidCoroutines : TopicRef() {
    override val title = "Coroutines"
  }

  data object KotlinSample : TopicRef() {
    override val title = "Kotlin sample"
  }
}
