package com.huhx0015.androidplayground.model

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class DataItem(
  val id: Int = Random.nextInt(),
  val title: String = Random.nextLong().toHexString(),
  val subtitle: String? = Random.nextLong().toHexString()
)

fun randomizeData(itemQuality: Int): List<DataItem> {
  val dataList: MutableList<DataItem> = mutableListOf()
  for (i in 0 until itemQuality) dataList.add(DataItem())
  return dataList
}