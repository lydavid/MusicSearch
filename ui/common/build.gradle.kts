import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "ly.david.musicsearch.ui.common"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.shared.domain)
                api(projects.shared.strings)
                implementation(projects.core.logging.api)

                implementation(compose.foundation)
                implementation(compose.materialIconsExtended)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.ui)

                implementation(libs.circuit.foundation)
                implementation(libs.circuit.overlay)
                implementation(libs.circuitx.overlays)
                implementation(libs.coil)
                implementation(libs.coil.compose)
                implementation(libs.koin.core)
                implementation(libs.lyricist.library)
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
                implementation(projects.core.coroutines)
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

    // Copied from https://github.com/slackhq/circuit/blob/e9955929fcbb2833622d74d4a738d70e14708613/samples/bottom-navigation/build.gradle.kts#L79
    targets.configureEach {
        if (platformType == KotlinPlatformType.androidJvm) {
            compilations.configureEach {
                compileTaskProvider.configure {
                    compilerOptions {
                        freeCompilerArgs.addAll(
                            "-P",
                            "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=" +
                                "ly.david.musicsearch.ui.common.screen.Parcelize",
                        )
                    }
                }
            }
        }
    }
}

dependencies {
    implementation(compose.preview)
    debugImplementation(compose.uiTooling)
}
