package com.huhx0015.androidplayground.feature.entrance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.huhx0015.androidplayground.feature.entrance.compose.EntranceScreen
import com.huhx0015.androidplayground.ui.theme.AndroidPlaygroundTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()

    setContent {
      AndroidPlaygroundTheme {
        EntranceScreen()
      }
    }
  }
}