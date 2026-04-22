package com.huhx0015.androidplayground.feature.android.compose.paymentworkflow.navigation

import kotlinx.serialization.Serializable

@Serializable
data object PaymentFormRoute

@Serializable
data object PaymentReviewRoute

@Serializable
data class PaymentResultRoute(val success: Boolean)
