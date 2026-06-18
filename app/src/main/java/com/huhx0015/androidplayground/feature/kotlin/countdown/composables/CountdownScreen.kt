package com.huhx0015.androidplayground.feature.kotlin.countdown.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.huhx0015.androidplayground.feature.kotlin.countdown.CountdownViewModel
import com.huhx0015.androidplayground.ui.theme.AndroidPlaygroundTheme

@Composable
fun CountdownScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: CountdownViewModel = viewModel()
    val time = viewModel.countDownFlow.collectAsState(initial = 10)

    AndroidPlaygroundTheme {
        Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = time.value.toString(),
                    fontSize = 30.sp
                )
            }
        }
    }
}
