plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "ly.david.musicsearch.core.parcelize"
}

kotlin {
    sourceSets {
//        val commonMain by getting {
//            dependencies {
//            }
//        }
//        val jvmMain by getting
//        val androidMain by getting {
////            dependencies {
////            }
//        }
    }
}
