plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.kotlin.multiplatform")
}

android {
    namespace = "ly.david.musicsearch.core.logging"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
            implementation(libs.koin.core)
            implementation(projects.core.logging.api)
        }
        }
        val androidMain by getting {
            dependencies {
            implementation(libs.timber)
        }
        }
    }
}
