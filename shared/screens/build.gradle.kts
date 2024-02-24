plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "ly.david.musicsearch.shared.screens"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.models)
                implementation(projects.core.parcelize)
                implementation(libs.circuit.foundation)
            }
        }
    }
}
