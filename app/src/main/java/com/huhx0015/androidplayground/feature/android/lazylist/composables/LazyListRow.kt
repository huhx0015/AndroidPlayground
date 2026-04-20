package com.huhx0015.androidplayground.feature.android.lazylist.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huhx0015.androidplayground.model.DataItem
import com.huhx0015.androidplayground.model.randomizeData

@Composable
/**
 * Displays a clickable row for a single lazy list item.
 */
internal fun LazyListRow(
    dataItem: DataItem,
    modifier: Modifier = Modifier,
    onRowClick: (Int) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(size = 8.dp)
            )
            .shadow(elevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onRowClick.invoke(dataItem.id) }
        ) {
            Column(
                modifier = Modifier.padding(all = 8.dp)
            ) {
                Text(
                    text = dataItem.title,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Normal,
                    color = Color.Black
                )
                dataItem.subtitle?.let { subtitle ->
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
@Preview
/**
 * Preview for [LazyListRow].
 */
private fun LazyListRowPreview() {
    LazyListRow(
        dataItem = randomizeData(1).first(),
        onRowClick = {}
    )
}