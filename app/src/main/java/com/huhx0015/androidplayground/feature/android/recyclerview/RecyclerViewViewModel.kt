package com.huhx0015.androidplayground.feature.android.recyclerview

import com.huhx0015.androidplayground.model.randomizeData
import com.huhx0015.androidplayground.core.architecture.BaseViewModel
import com.huhx0015.androidplayground.model.DataItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class RecyclerViewViewModel : BaseViewModel<RecyclerViewState, RecyclerViewIntent, RecyclerViewEvent>() {

  companion object {
    private const val VAL_DATA_ITEM_LIST_SIZE = 20
  }

  private val _state = MutableStateFlow(RecyclerViewState())
  private val _eventChannel = Channel<RecyclerViewEvent>(Channel.BUFFERED)

  override val state: StateFlow<RecyclerViewState>
    get() = _state
  override val events: Flow<RecyclerViewEvent> = _eventChannel.receiveAsFlow()

  // processIntent(): Routes each incoming UI intent to the matching handler.
  override suspend fun processIntent(intent: RecyclerViewIntent) {
    when (intent) {
      is RecyclerViewIntent.InitRecyclerViewIntent -> onInitRecyclerView()
      is RecyclerViewIntent.RecyclerViewButtonClickIntent ->
        onRecyclerViewButtonClicked(adapterType = intent.adapterType)
    }
  }

  // onInitRecyclerView(): Builds initial randomized data, updates state, and emits a refresh event.
  private suspend fun onInitRecyclerView() {
    val dataItemList = buildRandomizedData()
    _state.update { state -> state.copy(dataList = dataItemList) }
    _eventChannel.send(RecyclerViewEvent.RecyclerViewRefreshEvent)
  }

  // onRecyclerViewButtonClicked(): Regenerates data, switches adapter type, and emits a refresh event.
  private suspend fun onRecyclerViewButtonClicked(adapterType: RecyclerViewAdapterType) {
    val dataItemList = buildRandomizedData()
    _state.update { state ->
      state.copy(
        dataList = dataItemList,
        adapterType = adapterType
      )
    }
    _eventChannel.send(RecyclerViewEvent.RecyclerViewRefreshEvent)
  }

  // buildRandomizedData(): Creates a randomized list of data items with the configured list size.
  private fun buildRandomizedData(): List<DataItem> {
    val dataItemList = randomizeData(VAL_DATA_ITEM_LIST_SIZE)
    return dataItemList
  }
}