package com.huhx0015.androidplayground.feature.android.paymentworkflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.huhx0015.androidplayground.feature.android.compose.paymentworkflow.composables.PaymentWorkflowNavigationScreen
import com.huhx0015.androidplayground.ui.theme.AndroidPlaygroundTheme

class PaymentWorkflowActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      AndroidPlaygroundTheme {
        PaymentWorkflowNavigationScreen(onBackClick = ::finish)
      }
    }
  }
}
