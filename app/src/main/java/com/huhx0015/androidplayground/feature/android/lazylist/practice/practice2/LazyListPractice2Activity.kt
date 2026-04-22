package com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2.composables.LazyList2NavScreen
import com.huhx0015.androidplayground.ui.theme.AndroidPlaygroundTheme

/**
 *  ATTEMPT 2
 *  Tuesday, April 21, 2026
 *  Completion Time: 50:38
 */

class LazyListPractice2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidPlaygroundTheme {
                LazyList2NavScreen()
            }
        }
    }
}