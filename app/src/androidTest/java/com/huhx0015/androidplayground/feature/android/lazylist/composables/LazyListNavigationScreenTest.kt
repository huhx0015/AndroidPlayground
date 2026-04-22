package com.huhx0015.androidplayground.feature.android.lazylist.composables

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LazyListNavigationScreenTest {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Test
  fun lazyListNavigationScreen_displaysTopBarTitle() {
    composeTestRule.setContent {
      LazyListNavigationScreen(onBackButtonPress = {})
    }

    composeTestRule.onNodeWithText("LazyList").assertIsDisplayed()
  }

  @Test
  fun lazyListNavigationScreen_backPressAtRoot_invokesBackCallback() {
    var backPressCount = 0
    composeTestRule.setContent {
      LazyListNavigationScreen(onBackButtonPress = { backPressCount++ })
    }

    pressBack()
    composeTestRule.runOnIdle {
      assertEquals(1, backPressCount)
    }
  }

  @Test
  fun lazyListNavigationScreen_backPressFromDetails_onlyInvokesCallbackAtRoot() {
    var backPressCount = 0
    composeTestRule.setContent {
      LazyListNavigationScreen(onBackButtonPress = { backPressCount++ })
    }

    composeTestRule.waitUntil(timeoutMillis = 5_000L) {
      composeTestRule
        .onAllNodesWithText("Firion")
        .fetchSemanticsNodes().isNotEmpty()
    }

    composeTestRule.onNodeWithText("Firion").performClick()

    pressBack()
    composeTestRule.runOnIdle {
      assertEquals(0, backPressCount)
    }

    pressBack()
    composeTestRule.runOnIdle {
      assertEquals(1, backPressCount)
    }
  }
}
