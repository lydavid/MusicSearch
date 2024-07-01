plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
}

android {
    namespace = "ly.david.musicsearch.ui.image"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.models)
                implementation(projects.ui.core)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation(libs.koin.core)

                implementation(libs.coil.base)
                implementation(libs.coil.compose)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
            }
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
