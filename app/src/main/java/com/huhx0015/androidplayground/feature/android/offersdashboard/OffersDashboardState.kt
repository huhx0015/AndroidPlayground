package com.huhx0015.androidplayground.feature.android.compose.offersdashboard

data class OffersDashboardState(
  val isLoading: Boolean = true,
  val errorMessage: String? = null,
  val selectedCategory: com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OfferCategory = _root_ide_package_.com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OfferCategory.ALL,
  val offers: List<com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OfferItem> = emptyList(),
  val visibleOffers: List<com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OfferItem> = emptyList(),
)
