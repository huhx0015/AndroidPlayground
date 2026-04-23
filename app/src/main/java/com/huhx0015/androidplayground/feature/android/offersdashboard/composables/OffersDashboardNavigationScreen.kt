package com.huhx0015.androidplayground.feature.android.compose.offersdashboard.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.huhx0015.androidplayground.feature.android.offersdashboard.OfferCategory
import com.huhx0015.androidplayground.feature.android.offersdashboard.OfferItem
import com.huhx0015.androidplayground.feature.android.offersdashboard.OffersDashboardState
import com.huhx0015.androidplayground.feature.android.compose.offersdashboard.navigation.OfferDetailsRoute
import com.huhx0015.androidplayground.feature.android.compose.offersdashboard.navigation.OffersDashboardRoute
import com.huhx0015.androidplayground.feature.android.offersdashboard.OffersDashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
/**
 * Root navigation for offers: list with category chips, snackbars from [OffersDashboardViewModel.events],
 * and a simple offer detail route.
 */
@Composable
fun OffersDashboardNavigationScreen(
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val viewModel: OffersDashboardViewModel = viewModel()
  val navController = rememberNavController()
  val state by viewModel.state.collectAsStateWithLifecycle()
  val snackbarHostState = remember { SnackbarHostState() }
  val lifecycleOwner = LocalLifecycleOwner.current

  LaunchedEffect(viewModel, lifecycleOwner) {
    lifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
      viewModel.events.collect { message ->
        snackbarHostState.showSnackbar(message)
      }
    }
  }

  BackHandler {
    if (!navController.navigateUp()) {
      onBackClick()
    }
  }

  Scaffold(
    modifier = modifier,
    topBar = { TopAppBar(title = { Text("Offers Dashboard") }) },
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
  ) { paddingValues ->
    NavHost(
      navController = navController,
      startDestination = OffersDashboardRoute,
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
    ) {
      composable<OffersDashboardRoute> {
        OffersScreen(
          state = state,
          onCategorySelected = viewModel::onCategorySelected,
          onRetry = viewModel::loadOffers,
          onOfferClick = { navController.navigate(OfferDetailsRoute(it)) },
        )
      }
      composable<OfferDetailsRoute> { entry ->
        val route: OfferDetailsRoute = entry.toRoute()
        val offer = viewModel.getOfferById(route.offerId)
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Offer Detail")
          Text(offer?.title ?: "Offer missing")
        }
      }
    }
  }
}

/** Renders category filters, loading/error/empty states, and the scrollable visible offers list. */
@Composable
private fun OffersScreen(
  state: OffersDashboardState,
  onCategorySelected: (OfferCategory) -> Unit,
  onRetry: () -> Unit,
  onOfferClick: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      OfferCategory.entries.forEach { category ->
        AssistChip(
          onClick = { onCategorySelected(category) },
          label = { Text(category.name) },
        )
      }
    }

    when {
      state.isLoading -> Text("Loading offers...")
      state.errorMessage != null -> {
        Text(state.errorMessage)
        Button(onClick = onRetry) { Text("Retry") }
      }
      state.visibleOffers.isEmpty() -> Text("No offers found")
      else -> {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          items(items = state.visibleOffers, key = { it.id }) { offer ->
            Text(
              text = offer.title,
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onOfferClick(offer.id) }
                .padding(8.dp),
            )
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun OffersDashboardNavigationScreenPreview() {
  OffersDashboardNavigationScreen(onBackClick = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun OffersScreenPreview() {
  OffersScreen(
    state = OffersDashboardState(
      isLoading = false,
      selectedCategory = OfferCategory.ALL,
      offers = emptyList(),
      visibleOffers = listOf(
        OfferItem("offer-1", "5% cashback on groceries", OfferCategory.CASHBACK),
        OfferItem("offer-2", "Airport lounge pass", OfferCategory.TRAVEL),
      ),
    ),
    onCategorySelected = {},
    onRetry = {},
    onOfferClick = {},
  )
}
