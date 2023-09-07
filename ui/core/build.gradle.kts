plugins {
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    id("ly.david.android.library")
    id("com.android.library")
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

        val androidMain by getting {
            dependencies {
                implementation(compose.uiTooling)
            }
        }

        val jvmMain by getting {
            
        }
    }
}

android {
    namespace = "ly.david.ui.core"
}
