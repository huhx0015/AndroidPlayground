package com.huhx0015.androidplayground.feature.android.compose.paymentworkflow

/** UI state for the payment form: field values, inline validation, and submit flag. */
data class PaymentWorkflowState(
  val amount: String = "",
  val recipient: String = "",
  val amountError: String? = null,
  val recipientError: String? = null,
  val isSubmitting: Boolean = false,
) {
  /** True when amount parses to a value greater than zero and recipient is non-blank. */
  val isFormValid: Boolean
    get() = amount.toDoubleOrNull()?.let { it > 0 } == true && recipient.isNotBlank()
}
