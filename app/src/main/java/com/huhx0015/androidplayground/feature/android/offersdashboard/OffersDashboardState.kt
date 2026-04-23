package com.huhx0015.androidplayground.feature.android.offersdashboard

data class OffersDashboardState(
  val isLoading: Boolean = true,
  val errorMessage: String? = null,
  val selectedCategory: OfferCategory = OfferCategory.ALL,
  val offers: List<OfferItem> = emptyList(),
  val visibleOffers: List<OfferItem> = emptyList(),
)
