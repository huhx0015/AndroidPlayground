package com.huhx0015.androidplayground.feature.android.lazylist.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.huhx0015.androidplayground.feature.android.lazylist.LazyListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyListNavigationScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: LazyListViewModel = viewModel()
    val state = viewModel.state.collectAsState()
    val navController: NavController = rememberNavController()

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
        LazyListScreen(dataList = state.value.dataList)
    }
}

@Preview
@Composable
fun LazyListNavigationScreenPreview() {
    LazyListNavigationScreen()
}