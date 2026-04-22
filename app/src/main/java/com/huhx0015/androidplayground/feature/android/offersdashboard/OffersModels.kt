package com.huhx0015.androidplayground.feature.android.compose.offersdashboard

enum class OfferCategory {
  ALL,
  CASHBACK,
  TRAVEL,
}

data class OfferItem(
  val id: String,
  val title: String,
  val category: com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OfferCategory,
)

internal object OffersFakeRepository {
  fun getOffers(): List<com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OfferItem> {
    return listOf(
      _root_ide_package_.com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OfferItem(
        "offer-1",
        "5% cashback on groceries",
        _root_ide_package_.com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OfferCategory.CASHBACK
      ),
      _root_ide_package_.com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OfferItem(
        "offer-2",
        "Airport lounge pass",
        _root_ide_package_.com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OfferCategory.TRAVEL
      ),
      _root_ide_package_.com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OfferItem(
        "offer-3",
        "2% cashback at gas stations",
        _root_ide_package_.com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OfferCategory.CASHBACK
      ),
    )
  }
}
