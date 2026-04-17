package com.huhx0015.androidplayground.feature.android.lazylist.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
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
fun LazyListScreen(
    dataList: List<DataItem>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(text = "LazyList")
            },
            modifier = Modifier.fillMaxWidth(),
            actions = {},
            colors = TopAppBarColors(
                containerColor = Color.Red,
                scrolledContainerColor = Color.Red,
                navigationIconContentColor = Color.Red,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White
            )
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(dataList) { data ->
                Row(
                    modifier = Modifier.fillMaxWidth()
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
        }
    }
}

@Preview
@Composable
fun LazyListScreenPreview() {
    LazyListScreen(
        dataList = randomizeData(itemQuality = 100)
    )
}