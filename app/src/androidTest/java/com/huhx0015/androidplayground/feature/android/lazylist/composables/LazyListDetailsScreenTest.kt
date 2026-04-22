package com.huhx0015.androidplayground.feature.android.lazylist.composables

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huhx0015.androidplayground.model.DataItem
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LazyListDetailsScreenTest {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Test
  fun lazyListDetailsScreen_withDataItem_showsTitleAndSubtitle() {
    val dataItem = DataItem(id = 1, title = "Item Title", subtitle = "Item Subtitle")
    composeTestRule.setContent {
      LazyListDetailsScreen(dataItem = dataItem)
    }

    composeTestRule.onNodeWithText("Item Title").assertIsDisplayed()
    composeTestRule.onNodeWithText("Item Subtitle").assertIsDisplayed()
    composeTestRule.onNode(hasClickAction()).assertIsDisplayed()
  }

  @Test
  fun lazyListDetailsScreen_withNullDataItem_rendersClickableRow() {
    composeTestRule.setContent {
      LazyListDetailsScreen(dataItem = null)
    }

    composeTestRule.onNode(hasClickAction()).assertIsDisplayed()
  }
}
