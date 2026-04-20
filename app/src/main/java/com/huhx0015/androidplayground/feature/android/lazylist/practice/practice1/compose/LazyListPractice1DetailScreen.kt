package com.huhx0015.androidplayground.feature.android.lazylist.practice.practice1.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.huhx0015.androidplayground.feature.android.lazylist.practice.practice1.LazyListPractice1DataItem
import kotlin.random.Random

@Composable
internal fun DetailScreen(
    item: LazyListPractice1DataItem?,
    modifier: Modifier = Modifier
) {
    item?.let {
        ListRow(
            item = item,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.Top),
            onRowClick = {}
        )
    } ?: ErrorMessage(errorMessage = "No item was provided.")
}

@Preview
@Composable
private fun DetailScreenPreview() {
    DetailScreen(
        item = LazyListPractice1DataItem(
            id = Random.nextInt(),
            title = "Title",
            subtitle = "Subtitle"
        )
    )
}