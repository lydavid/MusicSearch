plugins {
    id("ly.david.android.library")
    id("ly.david.android.compose")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
}

android {
    namespace = "ly.david.musicsearch.feature.search"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.models)
                implementation(projects.domain)
                implementation(projects.strings)
                implementation(projects.ui.common)
                implementation(projects.ui.core)

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.preview)
                implementation(compose.ui)

                implementation(libs.circuit.foundation)
                implementation(libs.koin.core)
                implementation(libs.paging.common)
                implementation(libs.paging.compose)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(projects.ui.commonLegacy)
                implementation(libs.koin.androidx.compose)
                implementation(libs.androidx.paging.compose)
                implementation(libs.androidx.paging.runtime)
            }
        }
        val jvmMain by getting
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
