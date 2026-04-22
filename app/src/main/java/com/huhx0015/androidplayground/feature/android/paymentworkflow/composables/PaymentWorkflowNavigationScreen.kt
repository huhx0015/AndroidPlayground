package com.huhx0015.androidplayground.feature.android.compose.paymentworkflow.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.huhx0015.androidplayground.feature.android.compose.paymentworkflow.PaymentWorkflowState
import com.huhx0015.androidplayground.feature.android.compose.paymentworkflow.PaymentWorkflowViewModel
import com.huhx0015.androidplayground.feature.android.compose.paymentworkflow.navigation.PaymentFormRoute
import com.huhx0015.androidplayground.feature.android.compose.paymentworkflow.navigation.PaymentResultRoute
import com.huhx0015.androidplayground.feature.android.compose.paymentworkflow.navigation.PaymentReviewRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentWorkflowNavigationScreen(
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val viewModel: PaymentWorkflowViewModel = viewModel()
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
    topBar = { TopAppBar(title = { Text("Payment Flow") }) },
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
  ) { paddingValues ->
    NavHost(
      navController = navController,
      startDestination = PaymentFormRoute,
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
    ) {
      composable<PaymentFormRoute> {
        PaymentFormScreen(
          state = state,
          onAmountChanged = viewModel::onAmountChanged,
          onRecipientChanged = viewModel::onRecipientChanged,
          onContinueClick = { navController.navigate(PaymentReviewRoute) },
        )
      }
      composable<PaymentReviewRoute> {
        PaymentReviewScreen(
          amount = state.amount,
          recipient = state.recipient,
          onSubmitClick = {
            viewModel.submitPayment()
            navController.navigate(PaymentResultRoute(success = true))
          },
        )
      }
      composable<PaymentResultRoute> { backStackEntry ->
        val route: PaymentResultRoute = backStackEntry.toRoute()
        PaymentResultScreen(
          success = route.success,
          onDoneClick = onBackClick,
        )
      }
    }
  }
}

@Composable
private fun PaymentFormScreen(
  state: PaymentWorkflowState,
  onAmountChanged: (String) -> Unit,
  onRecipientChanged: (String) -> Unit,
  onContinueClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    OutlinedTextField(
      value = state.amount,
      onValueChange = onAmountChanged,
      modifier = Modifier.fillMaxWidth(),
      label = { Text("Amount") },
      isError = state.amountError != null,
      supportingText = { state.amountError?.let { Text(it) } },
    )
    OutlinedTextField(
      value = state.recipient,
      onValueChange = onRecipientChanged,
      modifier = Modifier.fillMaxWidth(),
      label = { Text("Recipient") },
      isError = state.recipientError != null,
      supportingText = { state.recipientError?.let { Text(it) } },
    )
    Button(
      onClick = onContinueClick,
      enabled = state.isFormValid,
      modifier = Modifier.fillMaxWidth(),
    ) {
      Text("Continue")
    }
  }
}

@Composable
private fun PaymentReviewScreen(
  amount: String,
  recipient: String,
  onSubmitClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    Text("Review Payment")
    Text("Amount: $amount")
    Text("Recipient: $recipient")
    Button(onClick = onSubmitClick, modifier = Modifier.fillMaxWidth()) {
      Text("Submit")
    }
  }
}

@Composable
private fun PaymentResultScreen(
  success: Boolean,
  onDoneClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    Text(if (success) "Payment sent" else "Payment failed")
    Button(onClick = onDoneClick, modifier = Modifier.fillMaxWidth()) {
      Text("Done")
    }
  }
}
