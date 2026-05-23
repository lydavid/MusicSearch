plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
}

kotlin {
    android {
        namespace = "ly.david.musicsearch.core.logging.impl"
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.logging.api)
                implementation(libs.koin.core)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.timber)
            }
        }
        val iosMain by getting
    }
}
