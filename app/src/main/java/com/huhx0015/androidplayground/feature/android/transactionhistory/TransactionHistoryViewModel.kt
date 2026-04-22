package com.huhx0015.androidplayground.feature.android.compose.transactionhistory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionHistoryViewModel(
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

  companion object {
    const val SEARCH_QUERY_KEY = "transaction_search_query"
    private const val SELECTED_FILTER_KEY = "transaction_selected_filter"
    private const val QUERY_DEBOUNCE_MS = 500L
  }

  private val _state = MutableStateFlow(
    TransactionHistoryState(
      query = savedStateHandle[SEARCH_QUERY_KEY] ?: "",
      selectedType = TransactionType.valueOf(
        savedStateHandle[SELECTED_FILTER_KEY] ?: TransactionType.ALL.name,
      ),
    ),
  )
  val state: StateFlow<TransactionHistoryState> = _state.asStateFlow()

  private val _events = MutableSharedFlow<String>()
  val events: SharedFlow<String> = _events.asSharedFlow()
  private var queryDebounceJob: Job? = null

  init {
    loadTransactions()
  }

  fun loadTransactions() {
    val all = TransactionFakeRepository.getTransactions()
    _state.update { current ->
      current.copy(
        isLoading = false,
        isError = false,
        transactions = all,
      )
    }
    applyFilters()
  }

  fun onQueryChanged(query: String) {
    savedStateHandle[SEARCH_QUERY_KEY] = query
    _state.update { it.copy(query = query) }
    queryDebounceJob?.cancel()
    queryDebounceJob = viewModelScope.launch {
      delay(QUERY_DEBOUNCE_MS)
      applyFilters()
    }
  }

  fun onFilterSelected(type: TransactionType) {
    savedStateHandle[SELECTED_FILTER_KEY] = type.name
    _state.update { it.copy(selectedType = type) }
    applyFilters()
  }

  fun getTransactionById(id: String): TransactionItem? {
    return _state.value.transactions.firstOrNull { it.id == id }
  }

  private fun applyFilters() {
    _state.update { current ->
      val filteredByType = current.transactions.filter { transaction ->
        current.selectedType == TransactionType.ALL || transaction.type == current.selectedType
      }
      val filtered = filteredByType.filter { transaction ->
        current.query.isBlank() || transaction.title.contains(current.query, ignoreCase = true)
      }
      current.copy(filteredTransactions = filtered)
    }
  }
}
