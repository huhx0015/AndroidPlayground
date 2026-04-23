package com.huhx0015.androidplayground.feature.android.offersdashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.huhx0015.androidplayground.feature.android.compose.offersdashboard.composables.OffersDashboardNavigationScreen
import com.huhx0015.androidplayground.ui.theme.AndroidPlaygroundTheme

/** Host activity for the offers dashboard Compose flow. */
class OffersDashboardActivity : ComponentActivity() {

  /** Sets up the themed root composable with back navigation finishing the activity. */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      AndroidPlaygroundTheme {
        OffersDashboardNavigationScreen(
          onBackClick = ::finish
        )
      }
    }
  }
}
