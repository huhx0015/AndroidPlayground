package com.huhx0015.androidplayground.feature.android.lazylist.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.huhx0015.androidplayground.feature.android.lazylist.LazyListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LazyListScreen(
    viewModel: LazyListViewModel,
    onRowClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    when {
        state.value.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        state.value.error != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.value.error?.message ?: "Error while loading data")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadData() }) {
                        Text(text = "Retry")
                    }
                }
            }
        }
        state.value.dataList.isEmpty() -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No items")
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier.fillMaxWidth()
            ) {
                items(state.value.dataList) { data ->
                    LazyListRow(
                        dataItem = data,
                        modifier = modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                        onRowClick = onRowClick
                    )
                }
                item {

                }
            }
        }
    }
}

@Preview
@Composable
fun LazyListScreenPreview() {
    val viewModel: LazyListViewModel = viewModel()
    LazyListScreen(
        viewModel = viewModel,
        onRowClick = {}
    )
}