package com.huhx0015.androidplayground.feature.android.lazylist.practice.practice1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.huhx0015.androidplayground.feature.android.lazylist.practice.practice1.compose.TestRunNavigationScreen

/**
 *  ATTEMPT 1
 *  Sunday, April 19, 2026
 *  Completion Time: 58:57
 */

class LazyListPractice1Activity : ComponentActivity() {

    val viewModel: LazyListPracticeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        renderCompose()
    }

    private fun renderCompose() {
        setContent {
            TestRunNavigationScreen()
        }
    }
}