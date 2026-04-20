package com.huhx0015.androidplayground.feature.android.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class ComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        renderCompose()
    }

    private fun renderCompose() {
        setContent {

        }
    }
}