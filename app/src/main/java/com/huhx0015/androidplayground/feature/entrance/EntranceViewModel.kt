package com.huhx0015.androidplayground.feature.entrance

import com.huhx0015.androidplayground.core.architecture.BaseViewModel
import com.huhx0015.androidplayground.feature.android.lazylist.LazyListActivity
import com.huhx0015.androidplayground.feature.android.recyclerview.RecyclerViewActivity
import com.huhx0015.androidplayground.feature.kotlin.coroutine.CoroutineActivity
import com.huhx0015.androidplayground.feature.kotlin.KotlinTopicActivity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class EntranceViewModel : BaseViewModel<EntranceState, EntranceIntent, EntranceEvent>() {

  private val _state = MutableStateFlow(EntranceState())
  override val state: StateFlow<EntranceState> = _state.asStateFlow()

  private val eventChannel = Channel<EntranceEvent>(Channel.BUFFERED)
  override val events: Flow<EntranceEvent> = eventChannel.receiveAsFlow()

  override suspend fun processIntent(intent: EntranceIntent) {
    when (intent) {
      is EntranceIntent.SelectTab -> {
        _state.update { it.copy(selectedTab = intent.tab) }
      }
      is EntranceIntent.OpenTopic -> {
        val target = when (intent.topic) {
          EntranceTopic.Coroutines -> CoroutineActivity::class
          EntranceTopic.KotlinSample -> KotlinTopicActivity::class
          EntranceTopic.LazyList -> LazyListActivity::class
          EntranceTopic.RecyclerView -> RecyclerViewActivity::class
        }
        eventChannel.send(EntranceEvent.StartActivity(target))
      }
    }
  }
}
