plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
}

kotlin {
    android {
        namespace = "ly.david.musicsearch.core.logging.api"
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
