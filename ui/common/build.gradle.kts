plugins {
    id("ly.david.android.library")
    alias(libs.plugins.kotlin.parcelize)
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.musicsearch.ui.common"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.shared.domain)
                implementation(projects.core.logging.api)

                implementation(compose.components.resources)
                implementation(compose.foundation)
                implementation(compose.materialIconsExtended)
                implementation(libs.compose.material3)
                implementation(compose.ui)

                implementation(libs.circuit.foundation)
                implementation(libs.circuit.overlay)
                implementation(libs.circuitx.overlays)
                implementation(libs.coil)
                implementation(libs.coil.compose)
                implementation(libs.koin.core)
                implementation(libs.materialKolor)
                implementation(libs.paging.common)
                implementation(libs.paging.compose)
                implementation(libs.zoomable)
            }
        }
        val jvmCommon by creating {
            dependsOn(commonMain)
        }
        val androidMain by getting {
            dependsOn(jvmCommon)
            dependencies {
                implementation(projects.test.image)
                implementation(libs.androidx.activity.compose)
                implementation(libs.coil.network.okhttp)
            }
        }
        val jvmMain by getting {
            dependsOn(jvmCommon)
            dependencies {
                implementation(libs.coil.network.okhttp)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.coil.network.ktor3)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(projects.testData)
                implementation(projects.ui.test.screenshot)
                implementation(libs.bundles.kotlinx.coroutines)
                implementation(libs.test.parameter.injector)
                implementation(libs.junit)
                implementation(libs.circuit.test)
                implementation(libs.robolectric)
                implementation(libs.androidx.paging.testing)
                implementation(libs.koin.test)
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = always
}

dependencies {
    implementation(compose.preview)
    debugImplementation(compose.uiTooling)
}
