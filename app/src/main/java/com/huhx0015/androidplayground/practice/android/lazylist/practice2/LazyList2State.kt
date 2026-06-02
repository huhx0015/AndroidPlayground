package com.huhx0015.androidplayground.practice.android.lazylist.practice2

import com.huhx0015.androidplayground.practice.android.lazylist.practice2.data.DataItem

data class LazyList2State(
    val itemList: List<DataItem> = emptyList(),
    val isLoading: Boolean = false,
    val selectedId: Int? = null,
)