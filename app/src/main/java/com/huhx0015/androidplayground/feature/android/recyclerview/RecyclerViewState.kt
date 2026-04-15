package com.huhx0015.androidplayground.feature.android.recyclerview

import com.huhx0015.androidplayground.model.DataItem
import com.huhx0015.androidplayground.core.architecture.BaseState

data class RecyclerViewState(
  val dataList: List<DataItem> = emptyList(),
  val adapterType: RecyclerViewAdapterType = RecyclerViewAdapterType.RECYCLER_VIEW
): BaseState

enum class RecyclerViewAdapterType {
  LIST_ADAPTER,
  RECYCLER_VIEW
}