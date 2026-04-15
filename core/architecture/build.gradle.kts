plugins {
  alias(libs.plugins.android.library)
}

android {
  namespace = "com.huhx0015.androidplayground.core.architecture"
  compileSdk {
    version = release(36)
  }

  defaultConfig {
    minSdk = 24
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}

dependencies {
  api(libs.androidx.lifecycle.viewmodel.ktx)
  api(libs.kotlinx.coroutines.core)
  testImplementation(libs.junit)
}
