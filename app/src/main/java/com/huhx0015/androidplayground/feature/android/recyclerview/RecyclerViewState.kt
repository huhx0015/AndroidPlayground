package com.huhx0015.androidplayground.feature.android.recyclerview

import com.example.model.DataItem
import com.huhx0015.androidplayground.core.architecture.BaseState

data class RecyclerViewState(
  val dataList: List<DataItem> = emptyList()
): BaseState