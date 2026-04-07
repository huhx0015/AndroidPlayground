package com.huhx0015.androidplayground.feature.entrance.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.huhx0015.androidplayground.feature.entrance.EntranceDestinations
import com.huhx0015.androidplayground.feature.entrance.EntranceIntent
import com.huhx0015.androidplayground.feature.entrance.EntranceViewModel
import com.huhx0015.androidplayground.feature.entrance.TopicRef
import com.huhx0015.androidplayground.feature.entrance.navigation.EntranceNavKey
import com.huhx0015.androidplayground.feature.entrance.navigation.toNavKey
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay

@Composable
fun EntranceScreen(viewModel: EntranceViewModel) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  val backStack = remember {
    mutableStateListOf<EntranceNavKey>(state.selectedTab.toNavKey())
  }

  LaunchedEffect(state.selectedTab) {
    val key = state.selectedTab.toNavKey()
    if (backStack.size != 1 || backStack[0] != key) {
      backStack.clear()
      backStack.add(key)
    }
  }

  NavigationSuiteScaffold(
    navigationSuiteItems = {
      EntranceDestinations.entries.forEach { destination ->
        item(
          icon = {
            Icon(
              destination.icon,
              contentDescription = destination.label,
            )
          },
          label = { Text(destination.label) },
          selected = destination == state.selectedTab,
          onClick = { viewModel.sendIntent(EntranceIntent.SelectTab(destination)) },
        )
      }
    },
  ) {
    NavDisplay(
      backStack = backStack,
      onBack = {
        if (backStack.size > 1) {
          backStack.removeLastOrNull()
        }
      },
      entryProvider = { key ->
        when (key) {
          EntranceNavKey.AndroidTab -> NavEntry(key) {
            TopicListScreen(
              topics = state.androidTopics,
              onTopicClick = { viewModel.sendIntent(EntranceIntent.OpenTopic(it)) },
            )
          }
          EntranceNavKey.KotlinTab -> NavEntry(key) {
            TopicListScreen(
              topics = state.kotlinTopics,
              onTopicClick = { viewModel.sendIntent(EntranceIntent.OpenTopic(it)) },
            )
          }
          EntranceNavKey.ExtraTab -> NavEntry(key) {
            ExtraPlaceholderContent()
          }
        }
      },
      modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(
          WindowInsets.safeDrawing.only(
            WindowInsetsSides.Horizontal + WindowInsetsSides.Top,
          ),
        )
    )
  }
}

@Composable
private fun TopicListScreen(
  topics: List<TopicRef>,
  onTopicClick: (TopicRef) -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    items(topics, key = { it::class.java.name }) { topic ->
      Button(
        onClick = { onTopicClick(topic) },
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text(topic.title)
      }
    }
  }
}

@Composable
private fun ExtraPlaceholderContent(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    Text("Coming soon")
  }
}
