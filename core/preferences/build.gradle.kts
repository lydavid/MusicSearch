plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.kotlin.multiplatform")
}

android {
    namespace = "ly.david.musicsearch.core.preferences"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
            implementation(libs.androidx.datastore.preferences.core)
            implementation(libs.koin.core)
            implementation(projects.core.coroutines)
            implementation(projects.shared.domain)
        }
        }
        val jvmCommon by creating {
            dependsOn(commonMain)
        }
        val jvmMain by getting {
            dependsOn(jvmCommon)
        }
        val androidMain by getting {
            dependsOn(jvmCommon)
            dependencies {
            implementation(libs.androidx.datastore.preferences.android)
        }
        }
    }
}
