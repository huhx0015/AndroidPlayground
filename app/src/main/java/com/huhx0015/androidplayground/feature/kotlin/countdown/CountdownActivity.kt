package com.huhx0015.androidplayground.feature.kotlin.countdown

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.huhx0015.androidplayground.feature.kotlin.countdown.composables.CountdownScreen

class CountdownActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CountdownScreen()
        }
    }
}
