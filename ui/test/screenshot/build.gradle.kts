plugins {
    // TODO: use com.android.library?
    id("ly.david.musicsearch.kotlin.multiplatform")
    id("ly.david.musicsearch.compose.multiplatform")
}



kotlin {
    android {
        namespace = "ly.david.musicsearch.ui.test.screenshot"
    }
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(projects.test.image)

                implementation(libs.coil.compose)
                implementation(libs.coil.test)
                implementation(libs.bundles.kotlinx.coroutines)

                implementation(libs.compose.runtime)

                implementation(libs.paparazzi)
                implementation(libs.test.parameter.injector)
            }
        }
    }
}
