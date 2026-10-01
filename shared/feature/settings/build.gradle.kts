plugins {
    id("ly.david.musicsearch.kotlin.multiplatform")
    id("ly.david.musicsearch.compose.multiplatform")
    alias(libs.plugins.build.config)
    alias(libs.plugins.paparazzi)
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
    android {
        namespace = "ly.david.musicsearch.shared.feature.settings"
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.ui.common)
                implementation(projects.shared.domain)

                implementation(libs.compose.components.resources)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)

                implementation(libs.circuit.foundation)
                implementation(libs.koin.core)
                implementation(libs.compose.colorpicker)
                implementation(libs.windowSizeClass)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.appauth)
                implementation(libs.androidx.activity.compose)

                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.compose.ui.tooling)
            }
        }
        val androidHostTest by getting {
            dependencies {
                implementation(projects.testData)
                implementation(projects.ui.test.screenshot)
                implementation(libs.test.parameter.injector)
                implementation(libs.bundles.kotlinx.coroutines)
                implementation(libs.junit)
                implementation(libs.koin.test)
                implementation(libs.circuit.test)
                implementation(libs.robolectric)
                implementation(libs.mockk)
            }
        }
    }
}
