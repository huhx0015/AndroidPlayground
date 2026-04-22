package com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2.data

import kotlinx.serialization.Serializable

@Serializable
data class DataItem(
    val id: Int,
    val title: String,
    val subtitle: String?
)