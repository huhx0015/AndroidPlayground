package com.huhx0015.androidplayground.feature.entrance.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.huhx0015.androidplayground.feature.entrance.EntranceDestinations
import com.huhx0015.androidplayground.ui.theme.AndroidPlaygroundTheme

@PreviewScreenSizes
@Composable
fun EntranceScreen() {
  var currentDestination by rememberSaveable { mutableStateOf(EntranceDestinations.HOME) }

  NavigationSuiteScaffold(
    navigationSuiteItems = {
      EntranceDestinations.entries.forEach {
        item(
          icon = {
            Icon(
              it.icon,
              contentDescription = it.label
            )
          },
          label = { Text(it.label) },
          selected = it == currentDestination,
          onClick = { currentDestination = it }
        )
      }
    }
  ) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
      Greeting(
        name = "Android",
        modifier = Modifier.padding(innerPadding)
      )
    }
  }
}

@Composable
private fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
  AndroidPlaygroundTheme {
    Greeting("Android")
  }
}