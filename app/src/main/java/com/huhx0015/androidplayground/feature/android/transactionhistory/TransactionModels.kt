package com.huhx0015.androidplayground.feature.android.compose.transactionhistory

enum class TransactionType {
  ALL,
  FOOD,
  SHOPPING,
  TRANSFER,
}

data class TransactionItem(
  val id: String,
  val title: String,
  val amount: String,
  val type: TransactionType,
)

internal object TransactionFakeRepository {
  /** Returns a static in-memory list of sample transactions for the history screen. */
  fun getTransactions(): List<TransactionItem> {
    return listOf(
      TransactionItem("tx-1", "Coffee shop", "-$6.50", TransactionType.FOOD),
      TransactionItem("tx-2", "Online store", "-$42.00", TransactionType.SHOPPING),
      TransactionItem("tx-3", "Peer transfer", "-$20.00", TransactionType.TRANSFER),
      TransactionItem("tx-4", "Grocery coffee beans", "-$14.20", TransactionType.FOOD),
      TransactionItem("tx-5", "Gift transfer", "+$85.00", TransactionType.TRANSFER),
    )
  }
}
