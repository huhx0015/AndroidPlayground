package com.huhx0015.androidplayground.feature.android.recyclerview

import com.example.model.randomizeData
import com.huhx0015.androidplayground.core.architecture.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class RecyclerViewViewModel : BaseViewModel<RecyclerViewState, RecyclerViewIntent, RecyclerViewEvent>() {

  companion object {
    private const val VAL_DATA_ITEM_LIST_SIZE = 10
  }

  private val _state = MutableStateFlow(RecyclerViewState())
  private val _events = MutableSharedFlow<RecyclerViewState>()
  private val _eventChannel = Channel<RecyclerViewEvent>(Channel.BUFFERED)

  override val state: StateFlow<RecyclerViewState>
    get() = _state
  override val events: Flow<RecyclerViewEvent> = _eventChannel.receiveAsFlow()

  override suspend fun processIntent(intent: RecyclerViewIntent) {
    when (intent) {
      RecyclerViewIntent.InitRecyclerView -> onInitRecyclerView()
    }
  }

  private fun onInitRecyclerView() {
    val dataItemList = randomizeData(VAL_DATA_ITEM_LIST_SIZE)
    _state.update { state ->
      state.copy(dataList = dataItemList)
    }
  }
}