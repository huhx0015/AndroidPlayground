package com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2.composables.LazyList2NavScreen
import com.huhx0015.androidplayground.ui.theme.AndroidPlaygroundTheme

/**
 *  ATTEMPT 2
 *  Tuesday, April 21, 2026
 *  Completion Time: 50:38
 */

class LazyListPractice2Activity : ComponentActivity() {

    private val viewModel: LazyListPractice2ViewModel by viewModels()

    companion object {
        private const val VAL_CONFIG_CHANGE = "config_change"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.loadData()
        }

        enableEdgeToEdge()
        setContent {
            AndroidPlaygroundTheme {
                LazyList2NavScreen()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(VAL_CONFIG_CHANGE, true)
    }
}