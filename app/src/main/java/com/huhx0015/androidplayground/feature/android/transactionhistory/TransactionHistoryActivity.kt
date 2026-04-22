package com.huhx0015.androidplayground.feature.android.transactionhistory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.huhx0015.androidplayground.feature.android.compose.transactionhistory.composables.TransactionHistoryNavigationScreen
import com.huhx0015.androidplayground.ui.theme.AndroidPlaygroundTheme

class TransactionHistoryActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      AndroidPlaygroundTheme {
        TransactionHistoryNavigationScreen(onBackClick = ::finish)
      }
    }
  }
}
