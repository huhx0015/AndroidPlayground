package com.huhx0015.androidplayground.feature.android.compose.paymentworkflow

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PaymentWorkflowViewModel(
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

  companion object {
    private const val AMOUNT_KEY = "payment_amount"
    private const val RECIPIENT_KEY = "payment_recipient"
  }

  private val _state = MutableStateFlow(
    PaymentWorkflowState(
      amount = savedStateHandle[AMOUNT_KEY] ?: "",
      recipient = savedStateHandle[RECIPIENT_KEY] ?: "",
    ),
  )
  val state: StateFlow<PaymentWorkflowState> = _state.asStateFlow()

  private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
  val events: SharedFlow<String> = _events.asSharedFlow()

  fun onAmountChanged(amount: String) {
    savedStateHandle[AMOUNT_KEY] = amount
    _state.update {
      it.copy(
        amount = amount,
        amountError = if (amount.toDoubleOrNull()?.let { value -> value > 0 } == true) null else "Enter amount > 0",
      )
    }
  }

  fun onRecipientChanged(recipient: String) {
    savedStateHandle[RECIPIENT_KEY] = recipient
    _state.update {
      it.copy(
        recipient = recipient,
        recipientError = if (recipient.isBlank()) "Recipient is required" else null,
      )
    }
  }

  fun submitPayment() {
    _state.update { it.copy(isSubmitting = true) }
    _state.update { it.copy(isSubmitting = false) }
    _events.tryEmit("Payment submitted")
  }
}
