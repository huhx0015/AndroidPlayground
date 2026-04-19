package com.huhx0015.androidplayground.feature.android.lazylist.navigation

sealed class Screen(val route: String) {
    data object List : Screen(route = "list")
    data object Detail : Screen(route = "detail/{$ARG_DATA_ITEM_ID}") {
        fun createRoute(dataItemId: Int): String = "detail/$dataItemId"
    }

    companion object {
        const val ARG_DATA_ITEM_ID = "dataItemId"
    }
}