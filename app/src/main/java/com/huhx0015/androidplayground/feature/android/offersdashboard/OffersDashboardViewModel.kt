package com.huhx0015.androidplayground.feature.android.offersdashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Holds offers list UI state and drives category filtering.
 *
 * [state] is screen data that should persist and be replayed to collectors; one-off UI
 * signals (e.g. snackbars) use [events] instead.
 */
class OffersDashboardViewModel(
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

  companion object {
    const val CATEGORY_KEY = "offers_category"
  }

  private val _state = MutableStateFlow(
    OffersDashboardState(
      selectedCategory = OfferCategory.valueOf(
        savedStateHandle[CATEGORY_KEY]
          ?: OfferCategory.ALL.name,
      ),
    ),
  )
  val state: StateFlow<OffersDashboardState> = _state.asStateFlow()

  /*
   * Ephemeral UI signals (e.g. snackbars): SharedFlow does not replay the last value to
   * new collectors the way StateFlow does, so past messages are not re-shown when the
   * screen becomes visible again. MutableSharedFlow is used internally to emit; [events]
   * exposes a read-only SharedFlow so only the ViewModel can emit.
   */
  private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
  val events: SharedFlow<String> = _events.asSharedFlow()

  init {
    loadOffers()
  }

  /** Loads fake offers from the repository, updates loading state, reapplies the category filter, and emits a refresh snackbar. */
  fun loadOffers() {
    _state.update { it.copy(isLoading = true, errorMessage = null) }
    val allOffers = OffersFakeRepository.getOffers()
    _state.update {
      it.copy(
        isLoading = false,
        offers = allOffers,
      )
    }
    applyCategoryFilter()
    _events.tryEmit("Offers refreshed")
  }

  /** Persists the category, updates state, and recomputes [OffersDashboardState.visibleOffers]. */
  fun onCategorySelected(category: OfferCategory) {
    savedStateHandle[CATEGORY_KEY] = category.name
    _state.update { it.copy(selectedCategory = category) }
    applyCategoryFilter()
  }

  /** Returns the offer with the given id from the full loaded list, or null if not found. */
  fun getOfferById(id: String): OfferItem? {
    return _state.value.offers.firstOrNull { it.id == id }
  }

  /** Filters [OffersDashboardState.offers] by [OffersDashboardState.selectedCategory] into [OffersDashboardState.visibleOffers]. */
  private fun applyCategoryFilter() {
    _state.update { current ->
      val visible = current.offers.filter { offer ->
        current.selectedCategory == OfferCategory.ALL || offer.category == current.selectedCategory
      }
      current.copy(visibleOffers = visible)
    }
  }
}
