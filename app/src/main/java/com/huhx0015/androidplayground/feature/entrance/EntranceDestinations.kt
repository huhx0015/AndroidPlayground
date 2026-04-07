package com.huhx0015.androidplayground.feature.entrance

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Extension
import androidx.compose.ui.graphics.vector.ImageVector

enum class EntranceDestinations(
  val label: String,
  val icon: ImageVector,
) {
  ANDROID("Android", Icons.Filled.Android),
  KOTLIN("Kotlin", Icons.Filled.Code),
  EXTRA("Extra", Icons.Filled.Extension),
}
