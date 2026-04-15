package com.example.model

import kotlin.random.Random

data class DataItem(
  val id: Int = Random.nextInt(),
  val title: String = Random.nextLong().toHexString(),
  val subtitle: String = Random.nextLong().toHexString()
)

fun randomizeData(itemQuality: Int): List<DataItem> {
  val dataList: MutableList<DataItem> = mutableListOf()
  for (i in 0 until itemQuality) dataList.add(DataItem())
  return dataList
}