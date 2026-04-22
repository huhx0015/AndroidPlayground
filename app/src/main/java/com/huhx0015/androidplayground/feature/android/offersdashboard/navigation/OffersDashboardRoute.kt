package com.huhx0015.androidplayground.feature.android.compose.offersdashboard.navigation

import kotlinx.serialization.Serializable

@Serializable
data object OffersDashboardRoute

@Serializable
data class OfferDetailsRoute(val offerId: String)
