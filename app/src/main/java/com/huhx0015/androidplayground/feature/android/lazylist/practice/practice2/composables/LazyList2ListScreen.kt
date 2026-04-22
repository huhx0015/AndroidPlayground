package com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2.LazyList2State
import com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2.data.DataItem

@Composable
fun LazyList2ListScreen(
    state: LazyList2State,
    rowClickAction: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            state.isLoading -> {
                Box(modifier = modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.size(64.dp))
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = state.itemList,
                        key = { it.id }
                    ) { item ->
                        LazyList2Row(
                            item = item,
                            rowClickAction = rowClickAction
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                    }
                }
            }
        }
    }
}

@Composable
internal fun LazyList2Row(
    item: DataItem,
    rowClickAction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { rowClickAction.invoke(item.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = item.title)
                item.subtitle?.let { Text(text = item.subtitle) }
            }
        }
    }
}

@Composable
@Preview
private fun LazyList2ListScreenPreview() {
    LazyList2ListScreen(
        state = LazyList2State(),
        rowClickAction = {}
    )
}