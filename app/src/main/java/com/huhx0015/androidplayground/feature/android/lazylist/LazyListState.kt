package com.huhx0015.androidplayground.feature.android.lazylist

import com.huhx0015.androidplayground.model.DataItem

data class LazyListState(
    val dataList: List<DataItem> = emptyList(),
    val selectedItemId: String? = null,
    val error: Error? = null,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false
)