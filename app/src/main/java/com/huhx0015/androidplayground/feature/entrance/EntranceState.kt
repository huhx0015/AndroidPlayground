package com.huhx0015.androidplayground.feature.entrance

import com.huhx0015.androidplayground.core.architecture.BaseState

data class EntranceState(
  val selectedTab: EntranceDestinations = EntranceDestinations.ANDROID,
  val androidTopics: List<TopicRef> = listOf(TopicRef.AndroidCoroutines),
  val kotlinTopics: List<TopicRef> = listOf(TopicRef.KotlinSample),
) : BaseState
