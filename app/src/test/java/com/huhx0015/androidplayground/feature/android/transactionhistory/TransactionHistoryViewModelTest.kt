package com.huhx0015.androidplayground.feature.android.compose.transactionhistory

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
class TransactionHistoryViewModelTest {

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun init_loadsFakeTransactions() = runTest {
    val viewModel = createViewModel(testScheduler)
    advanceUntilIdle()

    assertTrue(viewModel.state.value.transactions.isNotEmpty())
    assertEquals(false, viewModel.state.value.isLoading)
  }

  @Test
  fun onQueryChanged_persistsAndFiltersResults() = runTest {
    val savedStateHandle = SavedStateHandle()
    val viewModel = createViewModel(testScheduler, savedStateHandle)
    advanceUntilIdle()

    viewModel.onQueryChanged("coffee")
    advanceUntilIdle()

    assertEquals("coffee", savedStateHandle[TransactionHistoryViewModel.SEARCH_QUERY_KEY])
    assertTrue(viewModel.state.value.filteredTransactions.all { it.title.contains("coffee", true) })
  }

  private fun createViewModel(
    scheduler: TestCoroutineScheduler,
    savedStateHandle: SavedStateHandle = SavedStateHandle(),
  ): TransactionHistoryViewModel {
    Dispatchers.setMain(StandardTestDispatcher(scheduler))
    return TransactionHistoryViewModel(savedStateHandle = savedStateHandle)
  }
}
