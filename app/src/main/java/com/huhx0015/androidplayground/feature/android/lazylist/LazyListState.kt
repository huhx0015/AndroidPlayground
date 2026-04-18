package com.huhx0015.androidplayground.feature.android.lazylist

import com.huhx0015.androidplayground.model.DataItem

data class LazyListState(
    val dataList: List<DataItem> = emptyList(),
    val isLoading: Boolean = false
)