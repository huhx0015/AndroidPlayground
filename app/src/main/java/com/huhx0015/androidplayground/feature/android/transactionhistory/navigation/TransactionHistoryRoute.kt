package com.huhx0015.androidplayground.feature.android.transactionhistory.navigation

import kotlinx.serialization.Serializable

@Serializable
data object TransactionHistoryRoute

@Serializable
data class TransactionDetailsRoute(val transactionId: String)
