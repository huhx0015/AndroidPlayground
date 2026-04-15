package com.huhx0015.androidplayground.feature.android.recyclerview

import com.huhx0015.androidplayground.core.architecture.BaseIntent

sealed class RecyclerViewIntent: BaseIntent {
  data object InitRecyclerView: RecyclerViewIntent()
}