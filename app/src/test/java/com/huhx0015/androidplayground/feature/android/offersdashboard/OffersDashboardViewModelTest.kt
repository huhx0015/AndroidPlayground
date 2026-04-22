package com.huhx0015.androidplayground.feature.android.compose.offersdashboard

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OffersDashboardViewModelTest {

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun init_loadsOffers() = runTest {
    val viewModel = createViewModel(testScheduler)
    advanceUntilIdle()

    assertTrue(viewModel.state.value.offers.isNotEmpty())
    assertEquals(false, viewModel.state.value.isLoading)
  }

  @Test
  fun onCategorySelected_persistsSelection() = runTest {
    val savedStateHandle = SavedStateHandle()
    val viewModel = createViewModel(testScheduler, savedStateHandle)
    advanceUntilIdle()

    viewModel.onCategorySelected(OfferCategory.CASHBACK)

    assertEquals(
      OfferCategory.CASHBACK.name,
      savedStateHandle[OffersDashboardViewModel.CATEGORY_KEY],
    )
    assertEquals(OfferCategory.CASHBACK, viewModel.state.value.selectedCategory)
  }

  private fun createViewModel(
    scheduler: TestCoroutineScheduler,
    savedStateHandle: SavedStateHandle = SavedStateHandle(),
  ): OffersDashboardViewModel {
    Dispatchers.setMain(StandardTestDispatcher(scheduler))
    return OffersDashboardViewModel(savedStateHandle = savedStateHandle)
  }
}
