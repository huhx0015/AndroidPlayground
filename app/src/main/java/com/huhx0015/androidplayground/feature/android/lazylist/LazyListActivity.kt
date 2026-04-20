package com.huhx0015.androidplayground.feature.android.lazylist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.huhx0015.androidplayground.feature.android.lazylist.composables.LazyListNavigationScreen
import com.huhx0015.androidplayground.ui.theme.AndroidPlaygroundTheme

class LazyListActivity : ComponentActivity() {

    val viewModel: LazyListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initCompose()
    }

    private fun initCompose() {
        setContent {
            AndroidPlaygroundTheme {
                LazyListNavigationScreen()
            }
        }
    }
}