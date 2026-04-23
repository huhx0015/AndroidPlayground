package com.huhx0015.androidplayground.feature.android.offersdashboard

enum class OfferCategory {
  ALL,
  CASHBACK,
  TRAVEL,
}

data class OfferItem(
  val id: String,
  val title: String,
  val category: OfferCategory,
)

internal object OffersFakeRepository {
  /** Returns a static in-memory list of sample offers for the dashboard. */
  fun getOffers(): List<OfferItem> {
    return listOf(
      OfferItem(
        "offer-1",
        "5% cashback on groceries",
        OfferCategory.CASHBACK
      ),
      OfferItem(
        "offer-2",
        "Airport lounge pass",
        OfferCategory.TRAVEL
      ),
      OfferItem(
        "offer-3",
        "2% cashback at gas stations",
        OfferCategory.CASHBACK
      ),
    )
  }
}
