package com.huhx0015.androidplayground.feature.android.lazylist.practice.practice1.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.huhx0015.androidplayground.feature.android.lazylist.practice.practice1.LazyListPracticeNavigationRoute
import com.huhx0015.androidplayground.feature.android.lazylist.practice.practice1.LazyListPracticeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TestRunNavigationScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: LazyListPracticeViewModel = viewModel()
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    Scaffold(modifier = modifier,
       topBar = {
           TopAppBar(
               title = {
                   Text(text = "Test Run")
               },
               modifier = modifier.fillMaxWidth(),
           )
       }
    ) { padding ->
        NavHost(
            modifier = modifier.padding(padding),
            navController = navController,
            startDestination = LazyListPracticeNavigationRoute.ListScreen.route
        ) {
            // LIST:
            composable(route = LazyListPracticeNavigationRoute.ListScreen.route) {
                ListScreen(
                    state = state.value,
                    onRowClick = { id ->
                        viewModel.onItemSelected(id = id)
                        navController.navigate(LazyListPracticeNavigationRoute.DetailScreen.route)
                    }
                )
            }

            // DETAIL:
            composable(route = LazyListPracticeNavigationRoute.DetailScreen.route) {
                val selectedId = state.value.selectedItemId
                selectedId?.let {
                    val item = viewModel.getItemById(id = selectedId)
                    item?.let {
                        DetailScreen(item = item)
                    } ?: ErrorMessage(errorMessage = "No item found")
                } ?: ErrorMessage(errorMessage = "No id found")
            }
        }
    }
}

