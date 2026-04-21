package com.huhx0015.androidplayground.feature.android.lazylist.navigation

import kotlinx.serialization.Serializable

@Serializable
data object LazyListRoute

@Serializable
data class LazyListDetailsRoute(
    val itemId: Int
)