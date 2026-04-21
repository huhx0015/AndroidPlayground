plugins {
  alias(libs.plugins.kotlin.jvm)
  kotlin("plugin.serialization") version "2.2.10"
}

dependencies {
  implementation(libs.kotlinx.serialization.json)
  testImplementation(libs.junit)
}
