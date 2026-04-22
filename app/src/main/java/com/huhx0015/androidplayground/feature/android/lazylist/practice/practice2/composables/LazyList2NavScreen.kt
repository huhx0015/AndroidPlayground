package com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2.LazyListPractice2ViewModel
import com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2.navigation.LazyListNavKey

@Composable
fun LazyList2NavScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: LazyListPractice2ViewModel = viewModel()
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LazyListNavKey.List,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // LIST:
            composable<LazyListNavKey.List> {
                LazyList2ListScreen(
                    state = state.value,
                    rowClickAction = { id ->
                        viewModel.updateSelectedId(id = id)
                        navController.navigate(route = LazyListNavKey.Detail(itemId = id))
                    }
                )
            }
            // DETAIL:
            composable<LazyListNavKey.Detail> { key ->
                val route: LazyListNavKey.Detail = key.toRoute()
                val itemId = route.itemId
                val dataItem = viewModel.getDataItemById(itemId)
                dataItem?.let {
                    LazyList2DetailScreen(item = dataItem)
                } ?: run {
                    // TODO: Show error screen
                }
            }
        }
    }
}