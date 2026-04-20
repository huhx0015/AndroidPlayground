package com.huhx0015.androidplayground.feature.android.lazylist.practice.practice1.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huhx0015.androidplayground.feature.android.lazylist.practice.practice1.LazyListPractice1DataItem

@Composable
internal fun ErrorMessage(
    modifier: Modifier = Modifier,
    errorMessage: String?
) {
    Row(
        modifier = modifier
            .fillMaxWidth()) {
        Text(
            text = errorMessage ?: "Failed to fetch item"
        )
    }
}

@Composable
internal fun ListRow(
    item: LazyListPractice1DataItem,
    modifier: Modifier = Modifier,
    onRowClick: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
            .clickable(onClick = { onRowClick.invoke(item.id) })
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = item.title,
                fontSize = 14.sp,
                color = Color.Black
            )
            item.subtitle?.let {
                Text(
                    text = item.subtitle,
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }
        }
    }
}