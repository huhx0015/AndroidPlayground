package com.huhx0015.androidplayground.feature.android.lazylist.practice.practice1

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class LazyListPracticeViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    companion object {
        private const val STATE_SELECTED_ITEM_ID = "selected_item_id"
    }

    val _state: MutableStateFlow<LazyListPracticeState> = MutableStateFlow(
        LazyListPracticeState(
            selectedItemId = savedStateHandle[STATE_SELECTED_ITEM_ID]
        )
    )
    val state: StateFlow<LazyListPracticeState> = _state.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            val itemList = listOf(
                LazyListPractice1DataItem(
                    id = Random.nextInt(),
                    title = "Item 1",
                    subtitle = "First"
                ),
                LazyListPractice1DataItem(
                    id = Random.nextInt(),
                    title = "Item 2",
                    subtitle = "Second"
                ),
                LazyListPractice1DataItem(
                    id = Random.nextInt(),
                    title = "Item 3",
                    subtitle = "Third"
                ),
            )
            _state.update { state ->
                state.copy(
                    itemList = itemList
                )
            }
        }
    }

    fun onItemSelected(id: Int) {
        savedStateHandle[STATE_SELECTED_ITEM_ID] = id
        _state.update { state ->
            state.copy(
                selectedItemId = id
            )
        }
    }

    fun getItemById(id: Int): LazyListPractice1DataItem? {
        val itemList = state.value.itemList
        return itemList.firstOrNull { item ->
            id == item.id
        }
    }
}