package com.huhx0015.androidplayground.feature.kotlin.countdown

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CountdownViewModel: ViewModel() {

    // flow: Cold flow, only will be active as soon as there are collectors.
    val countDownFlow = flow<Int> {
        val startingValue = 10
        var currentValue = startingValue
        emit(startingValue) // Emit starting value before while loop.

        while (currentValue > 0) {
            delay(1000L) // Delay for 1 second.
            currentValue-- // Decrement the value.
            emit(currentValue) // Emits the updated currentValue.
        }
    }

    init {
        collectFlow()
    }

    private fun collectFlow() {
        viewModelScope.launch {
            // Collect: Executed for every emission.
            // CollectLatest: Executed for every emission, but if cancelled, only last emission wil
            // be emitted. Will cancel the old running emission,
            countDownFlow.collectLatest { time ->
                delay(1500L)
                println("The current time is $time")
                // Will only print out the last emission (0) because all others are being cancelled out.
            }
        }
    }

    private fun printCollectFlow() {
        viewModelScope.launch {
            // Collect: Executed for every emission.
            countDownFlow.collect { time ->
                delay(1500L)
                println("The current time is $time")
                // Will print out every single second, but at 1500L.
            }
        }
    }

    private fun printCollectLatestFlow() {
        viewModelScope.launch {
            // CollectLatest: Executed for every emission, but if cancelled, only last emission wil
            // be emitted. Will cancel the old running emission,
            countDownFlow.collectLatest { time ->
                delay(1500L)
                println("The current time is $time")
                // Will only print out the last emission (0) because all others are being cancelled out.
            }
        }
    }
}