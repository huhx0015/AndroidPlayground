package com.huhx0015.androidplayground.practice.android.lazylist.practice2.navigation

import kotlinx.serialization.Serializable

sealed class LazyListNavKey {

    @Serializable
    data object List : LazyListNavKey()

    @Serializable
    data class Detail(val itemId: Int) : LazyListNavKey()
}