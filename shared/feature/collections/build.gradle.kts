plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.build.config)
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ly.david.musicsearch.shared.feature.collection"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.models)
                implementation(projects.core.preferences)
                implementation(projects.domain)
                implementation(projects.strings)
                implementation(projects.ui.common)
                implementation(projects.ui.core)

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)

                implementation(libs.circuit.foundation)
                implementation(libs.circuit.overlay)
                implementation(libs.circuitx.overlays)
                implementation(libs.koin.core)
                implementation(libs.paging.common)
                implementation(libs.paging.compose)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.koin.androidx.compose)
                implementation(libs.koin.annotations)
                implementation(compose.preview)
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
    debugImplementation(libs.compose.ui.tooling)
//    ksp(libs.koin.ksp.compiler)
    add("kspAndroid", libs.koin.ksp.compiler)
}
