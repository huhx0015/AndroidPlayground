package com.huhx0015.androidplayground.feature.android.compose.paymentworkflow

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PaymentWorkflowViewModelTest {

  @Test
  fun isFormValid_returnsFalseWhenFieldsBlank() {
    val viewModel = PaymentWorkflowViewModel(savedStateHandle = SavedStateHandle())

    assertFalse(viewModel.state.value.isFormValid)
  }

  @Test
  fun onAmountChangedAndRecipientChanged_makeFormValid() {
    val viewModel = PaymentWorkflowViewModel(savedStateHandle = SavedStateHandle())

    viewModel.onAmountChanged("100.00")
    viewModel.onRecipientChanged("Jane")

    assertTrue(viewModel.state.value.isFormValid)
  }
}
