plugins {
    // Note that the order matters, because kotlin.multiplatform checks for existence of ly.david.android.library
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
}

android {
    namespace = "ly.david.musicsearch.ui.core"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.shared.strings)
                implementation(projects.shared.domain)

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(libs.lyricist.library)
                implementation(libs.materialKolor)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
            }
        }
    }
}
