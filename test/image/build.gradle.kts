plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
    id("ly.david.musicsearch.compose.multiplatform")
}

kotlin {
    android {
        namespace = "ly.david.musicsearch.test.image"
    }
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.coil.compose)
                implementation(libs.coil.test)
            }
        }
    }
}
