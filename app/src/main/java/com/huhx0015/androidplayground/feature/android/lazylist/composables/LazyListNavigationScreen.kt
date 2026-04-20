package com.huhx0015.androidplayground.feature.android.lazylist.composables

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                            // TODO: Need to fix finishing activity if at the top of the stack.
                            navController.navigateUp()
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
            startDestination = Screen.List.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            composable(route = Screen.List.route) {
                LazyListScreen(
                    viewModel = viewModel,
                    onRowClick = { dataItemId ->
                        viewModel.onItemSelected(itemId = dataItemId.toString())
                        navController.navigate(Screen.Detail.route)
                    }
                )
            }
            composable(route = Screen.Detail.route) {
                val selectedItemId = state.value.selectedItemId?.toIntOrNull()
                val dataItem = selectedItemId?.let { id ->
                    viewModel.getDataItemById(id)
                }
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