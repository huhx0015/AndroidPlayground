package com.huhx0015.androidplayground.feature.android.lazylist.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.huhx0015.androidplayground.model.DataItem

@Composable
internal fun LazyListDetailsScreen(
    dataItem: DataItem?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = dataItem?.title ?: "Title not found"
        )
        Text(
            text = dataItem?.subtitle ?: "Subtitle not found"
        )
    }
}

@Preview
@Composable
private fun LazyListDetailsScreenPreview() {
    LazyListDetailsScreen(dataItem = DataItem())
}