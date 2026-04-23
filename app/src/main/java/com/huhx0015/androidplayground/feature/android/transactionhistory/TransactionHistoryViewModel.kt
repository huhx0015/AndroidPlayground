package com.huhx0015.androidplayground.feature.android.compose.transactionhistory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Manages transaction list, search query, type filter, and derived filtered list.
 *
 * [state] holds durable UI data; one-off messages for snackbars use [events].
 */
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

  /*
   * Ephemeral UI signals (e.g. snackbars): SharedFlow does not replay the last value to
   * new collectors the way StateFlow does, so past messages are not re-shown when the
   * screen becomes visible again. MutableSharedFlow is used internally to emit; [events]
   * exposes a read-only SharedFlow so only the ViewModel can emit.
   */
  private val _events = MutableSharedFlow<String>()
  val events: SharedFlow<String> = _events.asSharedFlow()
  private val queryChanges = MutableSharedFlow<String>(extraBufferCapacity = 1)

  init {
    observeQueryChanges()
    loadTransactions()
  }

  /** Loads the fake transaction list into state and applies the current query and type filters. */
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

  /**
   * Updates the persisted and in-memory search query immediately; filtering runs after
   * [QUERY_DEBOUNCE_MS] of stability via [queryChanges].
   */
  fun onQueryChanged(query: String) {
    savedStateHandle[SEARCH_QUERY_KEY] = query
    _state.update { it.copy(query = query) }
    queryChanges.tryEmit(query)
  }

  /** Persists the transaction type filter, updates state, and recomputes the filtered list immediately. */
  fun onFilterSelected(type: TransactionType) {
    savedStateHandle[SELECTED_FILTER_KEY] = type.name
    _state.update { it.copy(selectedType = type) }
    applyFilters()
  }

  /** Looks up a transaction by id in the full (unfiltered) loaded list. */
  fun getTransactionById(id: String): TransactionItem? {
    return _state.value.transactions.firstOrNull { it.id == id }
  }

  /** Applies [TransactionHistoryState.selectedType] and [TransactionHistoryState.query] to populate [TransactionHistoryState.filteredTransactions]. */
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

  /** Collects debounced query emissions and triggers [applyFilters] after typing pauses. */
  private fun observeQueryChanges() {
    viewModelScope.launch {
      queryChanges
        .debounce(QUERY_DEBOUNCE_MS)
        .distinctUntilChanged()
        .collect {
          applyFilters()
        }
    }
  }
}
