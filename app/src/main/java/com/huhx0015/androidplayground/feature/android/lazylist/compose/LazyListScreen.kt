package com.huhx0015.androidplayground.feature.android.lazylist.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huhx0015.androidplayground.model.DataItem
import com.huhx0015.androidplayground.model.randomizeData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LazyListScreen(
    dataList: List<DataItem>,
    onRowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(dataList) { data ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRowClick.invoke() }
            ) {
                Column(
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    Text(
                        text = data.title,
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Normal,
                        color = Color.Black
                    )
                    Text(
                        text = data.subtitle,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        color = Color.Black
                    )
                }
            }
        }
        item {

        }
    }
}

@Preview
@Composable
fun LazyListScreenPreview() {
    LazyListScreen(
        dataList = randomizeData(itemQuality = 100),
        onRowClick = {}
    )
}