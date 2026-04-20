package com.huhx0015.androidplayground.feature.android.compose.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ComposeScreen(
    modifier: Modifier
) {
    val searchBarState = rememberSearchBarState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchBar(
            state = searchBarState,
            inputField = {

            }
        )

        LoadingProgressBar(
            color = Color.Cyan,
            size = 128.dp
        )
    }
}