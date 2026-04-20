package com.huhx0015.androidplayground.feature.android.lazylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.huhx0015.androidplayground.model.DataItem
import com.huhx0015.androidplayground.model.randomizeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LazyListViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val SELECTED_DATA_ITEM_ID = "selected_data_item_id"
    }

    private val _state: MutableStateFlow<LazyListState> = MutableStateFlow(
        LazyListState(
            selectedItemId = savedStateHandle[SELECTED_DATA_ITEM_ID]
        )
    )
    val state: StateFlow<LazyListState> = _state.asStateFlow()

    init {
        loadData()
    }

    /**
     * Loads the initial set of data for the lazy list screen.
     */
    internal fun loadData() {
        //loadJsonData() // Uncomment to use JSON data for LazyList instead.
        loadMockData() // Uncomment to use mock data for LazyList instead.
    }

    /**
     * Loads list data from JSON once deserialization support is implemented.
     */
    private fun loadJsonData() {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO: Implement JSON deserializer logic here.
        }
    }

    /**
     * Loads mock data used by the lazy list screen.
     */
    private fun loadMockData() {
        val dataList = randomizeData(itemQuality = 10)
        _state.update { state ->
            state.copy(dataList = dataList)
        }
    }

    /**
     * Loads the next page of data when the list reaches the bottom.
     */
    internal fun loadMoreData() {
        // loadMoreJsonData()
        loadMoreMockData()
    }

    /**
     * Appends additional mock items and toggles loading-more state.
     */
    private fun loadMoreMockData() {
        _state.update { state ->
            state.copy(isLoadingMore = true)
        }

        val dataList: MutableList<DataItem> = state.value.dataList.toMutableList()
        dataList.addAll(randomizeData(itemQuality = 10))

        _state.update { state ->
            state.copy(
                dataList = dataList,
                isLoadingMore = false
            )
        }
    }

    /**
     * Returns the currently loaded item matching the given id.
     */
    internal fun getDataItemById(id: Int): DataItem? {
        return _state.value.dataList.firstOrNull { it.id == id }
    }

    /**
     * Persists and updates the currently selected lazy list item id.
     */
    internal fun onItemSelected(itemId: String) {
        savedStateHandle[SELECTED_DATA_ITEM_ID] = itemId
        _state.update { it.copy(selectedItemId = itemId) }
    }
}