package com.huhx0015.androidplayground.feature.entrance

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

enum class EntranceDestinations(
  val label: String,
  val icon: ImageVector,
) {
  HOME("Home", Icons.Default.Home),
  FAVORITES("Favorites", Icons.Default.Favorite),
  PROFILE("Profile", Icons.Default.AccountBox),
}

