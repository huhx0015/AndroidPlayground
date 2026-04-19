package com.huhx0015.androidplayground.feature.android.lazylist.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.huhx0015.androidplayground.feature.android.lazylist.LazyListViewModel
import com.huhx0015.androidplayground.feature.android.lazylist.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyListNavigationScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: LazyListViewModel = viewModel()
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(text = "LazyList")
            },
            modifier = Modifier.fillMaxWidth(),
            actions = {},
            colors = TopAppBarColors(
                containerColor = Color.Red,
                scrolledContainerColor = Color.Red,
                navigationIconContentColor = Color.Red,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White
            )
        )
        NavHost(
            navController = navController,
            startDestination = Screen.List.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(route = Screen.List.route) {
                LazyListScreen(
                    dataList = state.value.dataList,
                    onRowClick = { dataItemId ->
                        navController.navigate(Screen.Detail.createRoute(dataItemId))
                    }
                )
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(
                    navArgument(Screen.ARG_DATA_ITEM_ID) {
                        type = NavType.IntType
                    }
                )
            ) { backstackEntry ->
                val dataItemId = backstackEntry.arguments?.getInt(Screen.ARG_DATA_ITEM_ID)
                val dataItem = dataItemId?.let(viewModel::getDataItemById)

                LazyListDetailsScreen(dataItem = dataItem)
            }
        }
    }
}

@Preview
@Composable
fun LazyListNavigationScreenPreview() {
    LazyListNavigationScreen()
}