package com.huhx0015.androidplayground.feature.android.lazylist.practice.practice1

data class LazyListPracticeState(
    val itemList: List<LazyListPractice1DataItem> = emptyList(),
    val selectedItemId: Int? = null,
    val isLoading: Boolean = false,
    val error: Error? = null
)