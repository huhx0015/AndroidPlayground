package com.huhx0015.androidplayground.feature.android.compose.transactionhistory

data class TransactionHistoryState(
  val isLoading: Boolean = true,
  val isError: Boolean = false,
  val query: String = "",
  val selectedType: TransactionType = TransactionType.ALL,
  val transactions: List<TransactionItem> = emptyList(),
  val filteredTransactions: List<TransactionItem> = emptyList(),
)
