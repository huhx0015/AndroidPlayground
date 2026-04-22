package com.huhx0015.androidplayground.feature.android.compose.transactionhistory.composables

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.huhx0015.androidplayground.feature.android.compose.transactionhistory.TransactionHistoryState
import com.huhx0015.androidplayground.feature.android.compose.transactionhistory.TransactionHistoryViewModel
import com.huhx0015.androidplayground.feature.android.compose.transactionhistory.TransactionType
import com.huhx0015.androidplayground.feature.android.compose.transactionhistory.navigation.TransactionDetailsRoute
import com.huhx0015.androidplayground.feature.android.compose.transactionhistory.navigation.TransactionHistoryRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryNavigationScreen(
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val viewModel: TransactionHistoryViewModel = viewModel()
  val navController = rememberNavController()
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
    topBar = { TopAppBar(title = { Text("Transaction") }) },
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
  ) { paddingValues ->
    NavHost(
      navController = navController,
      startDestination = TransactionHistoryRoute,
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
    ) {
      composable<TransactionHistoryRoute> {
        val state by viewModel.state.collectAsStateWithLifecycle()
        TransactionListScreen(
          state = state,
          onQueryChanged = viewModel::onQueryChanged,
          onFilterSelected = viewModel::onFilterSelected,
          onItemClick = { navController.navigate(TransactionDetailsRoute(it)) },
        )
      }
      composable<TransactionDetailsRoute> { backStackEntry ->
        val route: TransactionDetailsRoute = backStackEntry.toRoute()
        val item = viewModel.getTransactionById(route.transactionId)
        TransactionDetailsScreen(itemTitle = item?.title ?: "Missing transaction")
      }
    }
  }
}

@Composable
private fun TransactionListScreen(
  state: TransactionHistoryState,
  onQueryChanged: (String) -> Unit,
  onFilterSelected: (TransactionType) -> Unit,
  onItemClick: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    OutlinedTextField(
      value = state.query,
      onValueChange = onQueryChanged,
      modifier = Modifier.fillMaxWidth(),
      label = { Text("Search transaction") },
    )

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      TransactionType.entries.forEach { type ->
        AssistChip(
          onClick = { onFilterSelected(type) },
          label = { Text(type.name) },
        )
      }
    }

    when {
      state.isLoading -> Text("Loading...")
      state.isError -> Text("Something went wrong")
      state.filteredTransactions.isEmpty() -> Text("No transactions found")
      else -> {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          items(items = state.filteredTransactions, key = { it.id }) { transaction ->
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick(transaction.id) }
                .padding(8.dp),
            ) {
              Text(transaction.title)
              Text(transaction.amount)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun TransactionDetailsScreen(itemTitle: String, modifier: Modifier = Modifier) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(24.dp),
  ) {
    Text(text = "Transaction Details")
    Text(text = itemTitle)
  }
}
