package com.huhx0015.androidplayground.feature.kotlin.coroutine

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huhx0015.androidplayground.core.architecture.BaseViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

class CoroutineViewModel : ViewModel() {

  private val impl =
    object : BaseViewModel<CoroutineState, CoroutineIntent, CoroutineEvent>(viewModelScope) {
      private val _state = MutableStateFlow(CoroutineState())
      override val state: StateFlow<CoroutineState> = _state.asStateFlow()

      private val eventChannel = Channel<CoroutineEvent>(Channel.BUFFERED)
      override val events: Flow<CoroutineEvent> = eventChannel.receiveAsFlow()

      private val timeFmt: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")

      private var cancellableDemoJob: Job? = null
      private var flowDemoJob: Job? = null

      override suspend fun processIntent(intent: CoroutineIntent) {
        when (intent) {
          is CoroutineIntent.SelectSection -> {
            _state.update { it.copy(selectedSection = intent.section) }
          }
          CoroutineIntent.ClearLog -> {
            _state.update { it.copy(logLines = emptyList()) }
          }
          CoroutineIntent.RunSampleWork -> runSampleWork()
          CoroutineIntent.RunDispatcherDemo -> runDispatcherDemo()
          CoroutineIntent.RunAsyncParallelDemo -> runAsyncParallelDemo()
          CoroutineIntent.RunAsyncSequentialDemo -> runAsyncSequentialDemo()
          CoroutineIntent.StartLongRunningWork -> startLongRunningWork()
          CoroutineIntent.CancelLongRunningWork -> cancelLongRunningWork()
          CoroutineIntent.RunTryCatchExceptionDemo -> runTryCatchExceptionDemo()
          CoroutineIntent.RunCoroutineExceptionHandlerDemo -> runCoroutineExceptionHandlerDemo()
          CoroutineIntent.RunColdFlowTwoCollectorsDemo -> runColdFlowTwoCollectorsDemo()
          CoroutineIntent.RunSharedFlowTwoCollectorsDemo -> runSharedFlowTwoCollectorsDemo()
          CoroutineIntent.RunChannelBackpressureDemo -> runChannelBackpressureDemo()
          CoroutineIntent.RunCoroutineScopeFailureDemo -> runCoroutineScopeFailureDemo()
          CoroutineIntent.RunSupervisorScopeFailureDemo -> runSupervisorScopeFailureDemo()
          CoroutineIntent.RunNonCancellableCleanupDemo -> runNonCancellableCleanupDemo()
        }
      }

      private fun appendLog(message: String) {
        val stamp = LocalTime.now().format(timeFmt)
        _state.update { s ->
          val next = (s.logLines + "[$stamp] $message").takeLast(LOG_CAP)
          s.copy(logLines = next)
        }
      }

      private suspend fun runSampleWork() {
        _state.update { it.copy(isLoading = true, statusText = "Working…") }
        delay(1_500L)
        _state.update { it.copy(isLoading = false, statusText = "Done") }
        eventChannel.send(CoroutineEvent.ShowMessage("Sample coroutine finished"))
      }

      private suspend fun runDispatcherDemo() {
        appendLog("Before withContext: ${threadLabel()}")
        val checksum = withContext(Dispatchers.Default) {
          appendLog("Inside Default: ${threadLabel()}")
          var acc = 0L
          repeat(60_000) { acc += it }
          acc % 10_000
        }
        appendLog("After Default (back on Main for this continuation): ${threadLabel()} tail=$checksum")
        _state.update {
          it.copy(statusText = "Dispatcher: heavy work ran on Default; UI state updated on Main")
        }
        eventChannel.send(CoroutineEvent.ShowMessage("Dispatcher demo finished"))
      }

      private suspend fun runAsyncParallelDemo() {
        val t0 = SystemClock.elapsedRealtime()
        coroutineScope {
          val a = async { delay(800); "A" }
          val b = async { delay(800); "B" }
          val out = awaitAll(a, b)
          val elapsed = SystemClock.elapsedRealtime() - t0
          appendLog("Parallel awaitAll $out in ${elapsed}ms (~max of 800ms+800ms if sequential)")
        }
      }

      private suspend fun runAsyncSequentialDemo() {
        val t0 = SystemClock.elapsedRealtime()
        delay(800)
        delay(800)
        val elapsed = SystemClock.elapsedRealtime() - t0
        appendLog("Sequential delays in ${elapsed}ms (expect ~1600ms)")
      }

      private fun startLongRunningWork() {
        cancellableDemoJob?.cancel()
        cancellableDemoJob = this@CoroutineViewModel.viewModelScope.launch {
          try {
            _state.update { it.copy(cancellationRunning = true, cancellationProgress = 0) }
            repeat(30) { step ->
              ensureActive()
              _state.update { it.copy(cancellationProgress = step + 1) }
              delay(250L)
            }
            appendLog("Long work completed without cancellation")
          } catch (e: CancellationException) {
            appendLog("Coroutine cancelled: ${e::class.simpleName}")
            throw e
          } finally {
            _state.update { it.copy(cancellationRunning = false) }
          }
        }
      }

      private fun cancelLongRunningWork() {
        cancellableDemoJob?.cancel()
        cancellableDemoJob = null
        this@CoroutineViewModel.viewModelScope.launch {
          withContext(NonCancellable) {
            delay(200L)
            appendLog("NonCancellable block ran after cancel (simulated critical cleanup)")
          }
        }
      }

      private suspend fun runTryCatchExceptionDemo() {
        try {
          delay(50)
          error("Expected failure for try/catch demo")
        } catch (e: IllegalStateException) {
          appendLog("Caught in same coroutine: ${e.message}")
        }
        _state.update { it.copy(statusText = "Exceptions: try/catch around suspend call") }
      }

      private fun runCoroutineExceptionHandlerDemo() {
        val handler = CoroutineExceptionHandler { _, throwable ->
          appendLog("CoroutineExceptionHandler: ${throwable::class.simpleName}: ${throwable.message}")
        }
        this@CoroutineViewModel.viewModelScope.launch(handler) {
          delay(30)
          error("Failure delivered to CoroutineExceptionHandler")
        }
      }

      private fun runColdFlowTwoCollectorsDemo() {
        flowDemoJob?.cancel()
        flowDemoJob = this@CoroutineViewModel.viewModelScope.launch {
          appendLog("Cold: two concurrent collectors → upstream runs twice")
          val source = flow {
            repeat(4) { i ->
              delay(200L)
              emit("v$i")
            }
          }
          coroutineScope {
            launch { source.collect { appendLog("Cold collector A: $it") } }
            launch { source.collect { appendLog("Cold collector B: $it") } }
          }
          appendLog("Cold demo: both collectors finished")
        }
      }

      private fun runSharedFlowTwoCollectorsDemo() {
        flowDemoJob?.cancel()
        flowDemoJob = this@CoroutineViewModel.viewModelScope.launch {
          appendLog("shareIn: staggered second subscriber (replay=1 catches early tick)")
          val shareScope: CoroutineScope = this
          val shared = flow {
            repeat(6) { i ->
              delay(250L)
              emit("tick-$i")
            }
          }.shareIn(
            shareScope,
            SharingStarted.WhileSubscribed(5_000L),
            replay = 1,
          )
          coroutineScope {
            launch { shared.collect { appendLog("Shared collector A: $it") } }
            delay(80L)
            launch { shared.collect { appendLog("Shared collector B: $it") } }
          }
          appendLog("Shared demo: single upstream, multiple subscribers")
        }
      }

      private suspend fun runChannelBackpressureDemo() {
        appendLog("Bounded Channel(capacity=2): trySend may suspend or fail when full")
        val channel = Channel<Int>(capacity = 2)
        coroutineScope {
          val producer = launch {
            repeat(6) { i ->
              val result = channel.trySend(i)
              appendLog("trySend($i) → success=${result.isSuccess}")
              delay(40L)
            }
            channel.close()
          }
          val consumer = launch {
            delay(300L)
            for (value in channel) {
              appendLog("receive → $value")
              delay(350L)
            }
          }
          producer.join()
          consumer.join()
        }
      }

      private suspend fun runCoroutineScopeFailureDemo() {
        try {
          coroutineScope {
            launch {
              delay(120L)
              appendLog("coroutineScope child A finished OK")
            }
            launch {
              delay(40L)
              appendLog("coroutineScope child B throws")
              error("child B failed")
            }
          }
          appendLog("coroutineScope: should not reach here if failed child cancels scope")
        } catch (e: IllegalStateException) {
          appendLog("coroutineScope aggregated failure: ${e.message}")
        }
      }

      private suspend fun runSupervisorScopeFailureDemo() {
        supervisorScope {
          val good = async {
            delay(120L)
            appendLog("supervisorScope child A finished")
            "ok"
          }
          val bad = async {
            delay(40L)
            appendLog("supervisorScope child B throws")
            error("child B failed")
          }
          val g = good.await()
          val b = runCatching { bad.await() }.exceptionOrNull()
          appendLog("supervisorScope: A=$g B error=${b?.message} (sibling not cancelled by B)")
        }
      }

      private fun runNonCancellableCleanupDemo() {
        this@CoroutineViewModel.viewModelScope.launch {
          try {
            repeat(8) { step ->
              ensureActive()
              delay(120L)
              if (step == 3) {
                appendLog("Simulating cancel during work…")
                coroutineContext[Job]?.cancel()
              }
            }
          } catch (_: CancellationException) {
            appendLog("Outer coroutine saw cancellation")
          } finally {
            if (!isActive) {
              withContext(NonCancellable) {
                delay(200L)
                appendLog("finally + NonCancellable: cleanup still ran")
              }
            }
          }
        }
      }

      private fun threadLabel(): String = Thread.currentThread().name
    }

  val state: StateFlow<CoroutineState> get() = impl.state
  val events: Flow<CoroutineEvent> get() = impl.events
  fun sendIntent(intent: CoroutineIntent) = impl.sendIntent(intent)

  private companion object {
    const val LOG_CAP = 40
  }
}
