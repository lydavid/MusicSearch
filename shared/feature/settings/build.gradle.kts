plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.build.config)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.musicsearch.shared.feature.settings"
}

buildConfig {
    buildConfigField(
        name = "VERSION_CODE",
        value = project.properties["VERSION_CODE"] as String? ?: "",
    )
    buildConfigField(
        name = "VERSION_NAME",
        value = project.properties["VERSION_NAME"] as String? ?: "",
    )
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.ui.common)
                implementation(projects.shared.domain)

                implementation(compose.foundation)
                implementation(compose.materialIconsExtended)
                implementation(compose.material3)
                implementation(compose.ui)

                implementation(libs.circuit.foundation)
                implementation(libs.koin.core)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.appauth)

                implementation(libs.androidx.activity.compose)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(projects.ui.test.screenshot)
                implementation(libs.test.parameter.injector)
                implementation(libs.bundles.kotlinx.coroutines)
            }
        }
    }
}

dependencies {
    implementation(compose.preview)
    debugImplementation(compose.uiTooling)
}
