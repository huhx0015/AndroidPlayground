package com.huhx0015.androidplayground.feature.android.lazylist.compose

import androidx.lifecycle.ViewModel
import com.huhx0015.androidplayground.model.randomizeData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LazyListViewModel : ViewModel() {

    private val _state: MutableStateFlow<LazyListState> = MutableStateFlow(LazyListState())
    val state: StateFlow<LazyListState> = _state.asStateFlow()

    internal fun initData() {
        _state.update { state ->
            state.copy(
                dataList = randomizeData(itemQuality = 100)
            )
        }
    }
}