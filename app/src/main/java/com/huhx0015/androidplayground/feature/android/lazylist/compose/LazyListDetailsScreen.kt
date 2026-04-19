package com.huhx0015.androidplayground.feature.android.lazylist.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.huhx0015.androidplayground.model.DataItem

@Composable
internal fun LazyListDetailsScreen(
    dataItem: DataItem,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier) {
        Text(
            text = dataItem.title
        )
    }
}