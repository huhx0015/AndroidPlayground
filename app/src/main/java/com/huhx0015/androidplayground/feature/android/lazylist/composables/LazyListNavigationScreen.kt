package com.huhx0015.androidplayground.feature.android.lazylist.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.toRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.huhx0015.androidplayground.feature.android.lazylist.LazyListViewModel
import com.huhx0015.androidplayground.feature.android.lazylist.navigation.LazyListDetailsRoute
import com.huhx0015.androidplayground.feature.android.lazylist.navigation.LazyListRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
/**
 * Renders the lazy list navigation host with a shared top app bar.
 */
fun LazyListNavigationScreen(
    onBackButtonPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: LazyListViewModel = viewModel()
    val navController = rememberNavController()

    val handleBackNavigation = {
        if (!navController.navigateUp()) {
            onBackButtonPress()
        }
    }

    BackHandler {
        handleBackNavigation()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "LazyList")
                },
                colors = TopAppBarColors(
                    containerColor = Color.Red,
                    scrolledContainerColor = Color.Red,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            handleBackNavigation()
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = LazyListRoute,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            composable<LazyListRoute> {
                LazyListScreen(
                    viewModel = viewModel,
                    onRowClick = { dataItemId ->
                        navController.navigate(
                            route = LazyListDetailsRoute(itemId = dataItemId)
                        )
                    }
                )
            }
            composable<LazyListDetailsRoute> { backStackEntry ->
                val route: LazyListDetailsRoute = backStackEntry.toRoute()
                val dataItem = viewModel.getDataItemById(route.itemId)
                LazyListDetailsScreen(dataItem = dataItem)
            }
        }
    }
}

@Preview
@Composable
fun LazyListNavigationScreenPreview() {
    LazyListNavigationScreen(onBackButtonPress = {})
}