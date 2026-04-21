package com.huhx0015.androidplayground.feature.android.lazylist

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LazyListViewModelTest {

  @Test
  fun init_withSavedSelectedItemId_setsSelectedItemInState() {
    val viewModel = createViewModel(
      savedStateHandle = SavedStateHandle(mapOf("selected_data_item_id" to "7"))
    )

    assertEquals("7", viewModel.state.value.selectedItemId)
  }

  @Test
  fun onItemSelected_updatesStateAndSavedStateHandle() {
    val savedStateHandle = SavedStateHandle()
    val viewModel = createViewModel(savedStateHandle = savedStateHandle)

    viewModel.onItemSelected(itemId = "11")

    assertEquals("11", viewModel.state.value.selectedItemId)
    assertEquals("11", savedStateHandle.get<String>("selected_data_item_id"))
  }

  @Test
  fun loadData_loadsItemsFromJsonAndStopsLoadingState() {
    val viewModel = createViewModel()

    waitForCondition { viewModel.state.value.dataList.isNotEmpty() }

    assertFalse(viewModel.state.value.isLoading)
    assertEquals(17, viewModel.state.value.dataList.size)
    assertEquals("Firion", viewModel.state.value.dataList.first().title)
  }

  @Test
  fun loadMoreData_appendsItemsAndResetsLoadingMore() {
    val viewModel = createViewModel()
    waitForCondition { viewModel.state.value.dataList.isNotEmpty() }
    val initialSize = viewModel.state.value.dataList.size

    viewModel.loadMoreData()

    assertFalse(viewModel.state.value.isLoadingMore)
    assertTrue(viewModel.state.value.dataList.size > initialSize)
  }

  @Test
  fun getDataItemById_returnsExpectedItemOrNull() {
    val viewModel = createViewModel()
    waitForCondition { viewModel.state.value.dataList.isNotEmpty() }

    val existingItem = viewModel.getDataItemById(id = 1)
    val missingItem = viewModel.getDataItemById(id = -1)

    assertNotNull(existingItem)
    assertEquals("Firion", existingItem?.title)
    assertNull(missingItem)
  }

  private fun createViewModel(
    savedStateHandle: SavedStateHandle = SavedStateHandle()
  ): LazyListViewModel {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val application = context.applicationContext as Application
    return LazyListViewModel(
      application = application,
      savedStateHandle = savedStateHandle
    )
  }

  private fun waitForCondition(
    timeoutMs: Long = 5_000L,
    intervalMs: Long = 25L,
    condition: () -> Boolean
  ) {
    val start = System.currentTimeMillis()
    while (!condition()) {
      if (System.currentTimeMillis() - start >= timeoutMs) {
        throw AssertionError("Condition was not met within ${timeoutMs}ms")
      }
      Thread.sleep(intervalMs)
    }
  }
}
