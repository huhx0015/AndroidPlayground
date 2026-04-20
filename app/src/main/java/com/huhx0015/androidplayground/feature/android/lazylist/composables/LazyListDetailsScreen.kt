@file:OptIn(ExperimentalMaterial3Api::class)

package com.huhx0015.androidplayground.feature.android.lazylist.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.huhx0015.androidplayground.model.DataItem

@Composable
internal fun LazyListDetailsScreen(
    dataItem: DataItem?,
    modifier: Modifier = Modifier
) {
    LazyListRow(
        dataItem = dataItem ?: DataItem(),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .padding(16.dp),
        onRowClick = {}
    )
}

@Preview
@Composable
private fun LazyListDetailsScreenPreview() {
    LazyListDetailsScreen(dataItem = DataItem())
}