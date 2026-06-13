plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
}

kotlin {
    android {
        namespace = "ly.david.musicsearch.core.preferences"
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.domain)
                implementation(projects.core.logging.api)
                implementation(libs.koin.core)
                implementation(libs.androidx.datastore.preferences.core)
            }
        }
        val jvmCommon by creating {
            dependsOn(commonMain)
        }
        val jvmMain by getting {
            dependsOn(jvmCommon)
            dependencies {
                implementation(libs.multiplatform.paths)
            }
        }
        val androidMain by getting {
            dependsOn(jvmCommon)
            dependencies {
                implementation(libs.androidx.datastore.preferences.android)
            }
        }
    }
}
