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

    internal fun loadData() {
        //loadJsonData() // Uncomment to use JSON data for LazyList instead.
        loadMockData() // Uncomment to use mock data for LazyList instead.
    }

    private fun loadJsonData() {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO: Implement JSON deserializer logic here.
        }
    }

    private fun loadMockData() {
        val dataList = randomizeData(itemQuality = 100)
        _state.update { state ->
            state.copy(dataList = dataList)
        }
    }

    internal fun getDataItemById(id: Int): DataItem? {
        return _state.value.dataList.firstOrNull { it.id == id }
    }

    internal fun onItemSelected(itemId: String) {
        savedStateHandle[SELECTED_DATA_ITEM_ID] = itemId
        _state.update { it.copy(selectedItemId = itemId) }
    }
}