package com.huhx0015.androidplayground.feature.entrance.navigation

import com.huhx0015.androidplayground.feature.entrance.EntranceDestinations

sealed interface EntranceNavKey {
  data object AndroidTab : EntranceNavKey
  data object KotlinTab : EntranceNavKey
  data object ExtraTab : EntranceNavKey
}

fun EntranceDestinations.toNavKey(): EntranceNavKey = when (this) {
  EntranceDestinations.ANDROID -> EntranceNavKey.AndroidTab
  EntranceDestinations.KOTLIN -> EntranceNavKey.KotlinTab
  EntranceDestinations.EXTRA -> EntranceNavKey.ExtraTab
}

fun EntranceNavKey.toDestination(): EntranceDestinations = when (this) {
  EntranceNavKey.AndroidTab -> EntranceDestinations.ANDROID
  EntranceNavKey.KotlinTab -> EntranceDestinations.KOTLIN
  EntranceNavKey.ExtraTab -> EntranceDestinations.EXTRA
}
