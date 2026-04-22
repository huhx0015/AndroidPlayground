package com.huhx0015.androidplayground.feature.android.lazylist.composables

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.huhx0015.androidplayground.feature.android.lazylist.LazyListViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LazyListScreenTest {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Test
  fun lazyListScreen_displaysLoadedItems() {
    val viewModel = createViewModel()
    composeTestRule.setContent {
      LazyListScreen(
        viewModel = viewModel,
        onRowClick = {}
      )
    }

    composeTestRule.waitUntil(timeoutMillis = 5_000L) {
      viewModel.state.value.dataList.isNotEmpty()
    }

    composeTestRule.onNodeWithText("Firion").assertExists()
    composeTestRule.onNodeWithText("Rebel Swordsman").assertExists()
  }

  @Test
  fun lazyListScreen_onRowClickInvokesCallbackWithItemId() {
    val viewModel = createViewModel()
    var clickedId: Int? = null
    composeTestRule.setContent {
      LazyListScreen(
        viewModel = viewModel,
        onRowClick = { clickedId = it }
      )
    }

    composeTestRule.waitUntil(timeoutMillis = 5_000L) {
      viewModel.state.value.dataList.any { it.title == "Firion" }
    }

    composeTestRule.onNodeWithText("Firion").performClick()
    composeTestRule.runOnIdle {
      assertEquals(1, clickedId)
    }
  }

  private fun createViewModel(): LazyListViewModel {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val application = context.applicationContext as Application
    return LazyListViewModel(
      application = application,
      savedStateHandle = SavedStateHandle()
    )
  }
}
