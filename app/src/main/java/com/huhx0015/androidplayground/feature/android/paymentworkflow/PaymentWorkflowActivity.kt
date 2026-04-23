package com.huhx0015.androidplayground.feature.android.paymentworkflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.huhx0015.androidplayground.feature.android.compose.paymentworkflow.composables.PaymentWorkflowNavigationScreen
import com.huhx0015.androidplayground.ui.theme.AndroidPlaygroundTheme

/** Host activity for the payment workflow Compose navigation. */
class PaymentWorkflowActivity : ComponentActivity() {

  /** Sets up the themed payment flow composable; back from the root finishes the activity. */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      AndroidPlaygroundTheme {
        PaymentWorkflowNavigationScreen(onBackClick = ::finish)
      }
    }
  }
}
