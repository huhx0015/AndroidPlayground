package com.huhx0015.androidplayground.feature.android.lazylist.practice.practice1.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.huhx0015.androidplayground.feature.android.lazylist.practice.practice1.LazyListPracticeState

@Composable
internal fun ListScreen(
    state: LazyListPracticeState,
    modifier: Modifier = Modifier,
    onRowClick: (Int) -> Unit
) {
    when {
        state.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        state.error != null -> {
            ErrorMessage(
                errorMessage = state.error.message
            )
        }
        state.itemList.isEmpty() -> {
            ErrorMessage(
                errorMessage = "List is empty"
            )
        }
        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                items(state.itemList) { item ->
                    ListRow(
                        item = item,
                        onRowClick = onRowClick
                    )
                }
            }
        }
    }
}