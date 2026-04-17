package com.huhx0015.androidplayground.feature.android.lazylist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.huhx0015.androidplayground.feature.android.lazylist.compose.LazyListScreen
import com.huhx0015.androidplayground.model.randomizeData

class LazyListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initCompose()
    }

    private fun initCompose() {
        setContent {
            LazyListScreen(
                dataList = randomizeData(itemQuality = 100)
            )
        }
    }
}