plugins {
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    id("ly.david.android.library")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.preview)
            }
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

android {
    namespace = "ly.david.ui.core"
}
