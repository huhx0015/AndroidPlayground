package com.huhx0015.androidplayground.feature.android.recyclerview

import com.huhx0015.androidplayground.core.architecture.BaseIntent

sealed class RecyclerViewIntent: BaseIntent {
  data object InitRecyclerViewIntent: RecyclerViewIntent()
  data class RecyclerViewButtonClickIntent(val adapterType: RecyclerViewAdapterType): RecyclerViewIntent()
}