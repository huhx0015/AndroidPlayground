package com.huhx0015.androidplayground.feature.android.lazylist.practice.practice2.data

import android.content.Context
import kotlinx.serialization.json.Json

object JsonUtils {

    fun loadDataFromJsonAsset(context: Context, fileName: String): List<DataItem> {
        val jsonString = context.assets.open(fileName)
            .bufferedReader()
            .use { it.readText() }
        return Json.decodeFromString(jsonString)
    }
}