package com.huhx0015.androidplayground.feature.android.compose.paymentworkflow

data class PaymentWorkflowState(
  val amount: String = "",
  val recipient: String = "",
  val amountError: String? = null,
  val recipientError: String? = null,
  val isSubmitting: Boolean = false,
) {
  val isFormValid: Boolean
    get() = amount.toDoubleOrNull()?.let { it > 0 } == true && recipient.isNotBlank()
}
