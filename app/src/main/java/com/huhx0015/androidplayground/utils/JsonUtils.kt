package com.huhx0015.androidplayground.utils

import android.content.Context
import com.huhx0015.androidplayground.model.DataItem
import kotlinx.serialization.json.Json

object JsonUtils {

    fun loadDataFromAsset(context: Context, fileName: String): List<DataItem> {
        val jsonString = context.assets.open(fileName)
            .bufferedReader()
            .use { it.readText() }
        return Json.decodeFromString(jsonString)
    }
}