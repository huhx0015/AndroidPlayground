package com.huhx0015.androidplayground.feature.android.lazylist.practice.practice1

sealed class LazyListPracticeNavigationRoute(val route: String) {
    data object ListScreen: LazyListPracticeNavigationRoute(route = "list")
    data object DetailScreen: LazyListPracticeNavigationRoute(route = "details")
}