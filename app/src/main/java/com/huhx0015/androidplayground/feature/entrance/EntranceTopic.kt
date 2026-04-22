package com.huhx0015.androidplayground.feature.entrance

sealed class EntranceTopic {
  abstract val title: String

  data object Coroutines : EntranceTopic() {
    override val title = "Coroutines"
  }

  data object KotlinSample : EntranceTopic() {
    override val title = "Kotlin sample"
  }

  data object LazyList : EntranceTopic() {
    override val title = "LazyList"
  }

  data object RecyclerView : EntranceTopic() {
    override val title: String = "RecyclerView"
  }

  data object InterviewTransactions : EntranceTopic() {
    override val title: String = "Transaction"
  }

  data object InterviewPaymentFlow : EntranceTopic() {
    override val title: String = "Payment Flow"
  }

  data object InterviewOffers : EntranceTopic() {
    override val title: String = "Offers Dashboard"
  }
}
