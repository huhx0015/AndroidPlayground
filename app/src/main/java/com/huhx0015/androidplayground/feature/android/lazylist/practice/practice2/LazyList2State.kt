package com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2

import com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2.data.DataItem

data class LazyList2State(
    val itemList: List<DataItem> = emptyList(),
    val isLoading: Boolean = false,
    val selectedId: Int? = null,
)