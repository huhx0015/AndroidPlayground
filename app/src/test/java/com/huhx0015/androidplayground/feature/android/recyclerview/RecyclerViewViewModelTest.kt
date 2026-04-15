package com.huhx0015.androidplayground.feature.android.recyclerview

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
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
class RecyclerViewViewModelTest {

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun initialState_returnsDefaultStateValues() = runTest {
    val viewModel = createViewModel(testScheduler)

    assertEquals(RecyclerViewAdapterType.RECYCLER_VIEW, viewModel.state.value.adapterType)
    assertTrue(viewModel.state.value.dataList.isEmpty())
  }

  @Test
  fun sendIntent_withInitRecyclerViewIntent_triggersInitRecyclerViewFlow() = runTest {
    val viewModel = createViewModel(testScheduler)
    val eventDeferred = async { viewModel.events.first() }

    viewModel.sendIntent(RecyclerViewIntent.InitRecyclerViewIntent)
    advanceUntilIdle()

    assertEquals(RecyclerViewAdapterType.RECYCLER_VIEW, viewModel.state.value.adapterType)
    assertEquals(20, viewModel.state.value.dataList.size)
    assertEquals(RecyclerViewEvent.RecyclerViewRefreshEvent, eventDeferred.await())
  }

  @Test
  fun sendIntent_withRecyclerViewButtonClickIntentForListAdapter_triggersButtonClickFlow() = runTest {
    val viewModel = createViewModel(testScheduler)
    val eventDeferred = async { viewModel.events.first() }

    viewModel.sendIntent(
      RecyclerViewIntent.RecyclerViewButtonClickIntent(
        adapterType = RecyclerViewAdapterType.LIST_ADAPTER
      )
    )
    advanceUntilIdle()

    assertEquals(RecyclerViewAdapterType.LIST_ADAPTER, viewModel.state.value.adapterType)
    assertEquals(20, viewModel.state.value.dataList.size)
    assertEquals(RecyclerViewEvent.RecyclerViewRefreshEvent, eventDeferred.await())
  }

  @Test
  fun sendIntent_withRecyclerViewButtonClickIntentForRecyclerView_triggersButtonClickFlow() = runTest {
    val viewModel = createViewModel(testScheduler)
    val eventDeferred = async { viewModel.events.first() }

    viewModel.sendIntent(
      RecyclerViewIntent.RecyclerViewButtonClickIntent(
        adapterType = RecyclerViewAdapterType.RECYCLER_VIEW
      )
    )
    advanceUntilIdle()

    assertEquals(RecyclerViewAdapterType.RECYCLER_VIEW, viewModel.state.value.adapterType)
    assertEquals(20, viewModel.state.value.dataList.size)
    assertEquals(RecyclerViewEvent.RecyclerViewRefreshEvent, eventDeferred.await())
  }

  private fun createViewModel(testScheduler: TestCoroutineScheduler): RecyclerViewViewModel {
    Dispatchers.setMain(StandardTestDispatcher(testScheduler))
    return RecyclerViewViewModel()
  }
}
