package com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2.data.DataItem
import com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2.data.JsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LazyListPractice2ViewModel(
    application: Application,
    val savedStateHandle: SavedStateHandle
): AndroidViewModel(application = application) {

    val _state: MutableStateFlow<LazyList2State> = MutableStateFlow(
        LazyList2State(
            selectedId = savedStateHandle[KEY_SELECTED_ID]
        )
    )
    val state: StateFlow<LazyList2State> = _state.asStateFlow()

    companion object {
        const val KEY_SELECTED_ID = "selectedId"
    }

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            val itemList = JsonUtils.loadDataFromJsonAsset(context = application, fileName = "data.json")
            _state.update { state ->
                state.copy(itemList = itemList)
            }
        }
    }

    fun getDataItemById(id: Int): DataItem? {
        val dataList = state.value.itemList
        val dataItem = dataList.firstOrNull { data ->
            data.id == id
        }
        return dataItem
    }

    fun updateSelectedId(id: Int) {
        savedStateHandle[KEY_SELECTED_ID] = id
        _state.update { state ->
            state.copy(selectedId = id)
        }
    }
}