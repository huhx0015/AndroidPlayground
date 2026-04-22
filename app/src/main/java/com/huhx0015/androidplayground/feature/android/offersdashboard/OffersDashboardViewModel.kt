package com.huhx0015.androidplayground.feature.android.compose.offersdashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OffersDashboardViewModel(
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

  companion object {
    const val CATEGORY_KEY = "offers_category"
  }

  private val _state = MutableStateFlow(
    _root_ide_package_.com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OffersDashboardState(
      selectedCategory = _root_ide_package_.com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OfferCategory.valueOf(
        savedStateHandle[CATEGORY_KEY]
          ?: _root_ide_package_.com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OfferCategory.ALL.name,
      ),
    ),
  )
  val state: StateFlow<com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OffersDashboardState> = _state.asStateFlow()
  private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
  val events: SharedFlow<String> = _events.asSharedFlow()

  init {
    loadOffers()
  }

  fun loadOffers() {
    _state.update { it.copy(isLoading = true, errorMessage = null) }
    val allOffers = _root_ide_package_.com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OffersFakeRepository.getOffers()
    _state.update {
      it.copy(
        isLoading = false,
        offers = allOffers,
      )
    }
    applyCategoryFilter()
    _events.tryEmit("Offers refreshed")
  }

  fun onCategorySelected(category: com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OfferCategory) {
    savedStateHandle[CATEGORY_KEY] = category.name
    _state.update { it.copy(selectedCategory = category) }
    applyCategoryFilter()
  }

  fun getOfferById(id: String): com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OfferItem? {
    return _state.value.offers.firstOrNull { it.id == id }
  }

  private fun applyCategoryFilter() {
    _state.update { current ->
      val visible = current.offers.filter { offer ->
        current.selectedCategory == _root_ide_package_.com.huhx0015.androidplayground.feature.android.compose.offersdashboard.OfferCategory.ALL || offer.category == current.selectedCategory
      }
      current.copy(visibleOffers = visible)
    }
  }
}
