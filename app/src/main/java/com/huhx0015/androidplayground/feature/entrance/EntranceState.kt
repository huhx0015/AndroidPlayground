package com.huhx0015.androidplayground.feature.entrance

import com.huhx0015.androidplayground.core.architecture.BaseState

data class EntranceState(
  val selectedTab: EntranceDestinations = EntranceDestinations.ANDROID,
  val androidTopics: List<EntranceTopic> = listOf(EntranceTopic.LazyList, EntranceTopic.RecyclerView),
  val kotlinTopics: List<EntranceTopic> = listOf(EntranceTopic.Coroutines),
) : BaseState
