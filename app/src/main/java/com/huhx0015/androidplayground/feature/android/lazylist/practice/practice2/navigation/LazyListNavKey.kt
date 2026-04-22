package com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2.navigation

import kotlinx.serialization.Serializable

sealed class LazyListNavKey {

    @Serializable
    data object List : LazyListNavKey()

    @Serializable
    data class Detail(val itemId: Int) : LazyListNavKey()
}