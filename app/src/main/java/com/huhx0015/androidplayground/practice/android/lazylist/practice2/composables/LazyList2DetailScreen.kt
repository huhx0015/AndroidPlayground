package com.huhx0015.androidplayground.practice.android.lazylist.practice2.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.huhx0015.androidplayground.practice.android.lazylist.practice2.data.DataItem

@Composable
fun LazyList2DetailScreen(
    item: DataItem,
    modifier: Modifier = Modifier
) {
    LazyList2Row(
        item = item,
        rowClickAction = {},
        modifier = modifier.fillMaxWidth()
    )
}