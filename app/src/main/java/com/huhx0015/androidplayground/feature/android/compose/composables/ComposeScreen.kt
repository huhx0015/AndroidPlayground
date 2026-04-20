package com.huhx0015.androidplayground.feature.android.compose.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun ComposeScreen(
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LoadingProgressBar(
            color = Color.Cyan,
            size = 128.dp
        )
    }
}