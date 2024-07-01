import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ly.david.musicsearch.ui.common"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.core.models)
                api(projects.ui.core)
                implementation(projects.data.coverart)
                implementation(projects.data.musicbrainz)
                implementation(projects.core.preferences)
                implementation(projects.core.logging.api)
                implementation(projects.shared.domain)
                implementation(projects.ui.image)

                implementation(compose.foundation)
                implementation(compose.materialIconsExtended)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.ui)

                implementation(libs.circuit.foundation)
                implementation(libs.circuit.overlay)
                implementation(libs.circuitx.overlays)
                implementation(libs.koin.annotations)
                implementation(libs.koin.core)
                implementation(libs.lyricist.library)
                implementation(libs.paging.common)
                implementation(libs.paging.compose)
            }
        }
        val jvmCommon by creating {
            dependsOn(commonMain)
        }
        val androidMain by getting {
            dependsOn(jvmCommon)
            dependencies {
                implementation(libs.androidx.activity.compose)
            }
        }
        val jvmMain by getting {
            dependsOn(jvmCommon)
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(projects.ui.test.image)
                implementation(projects.ui.test.screenshot)
                implementation(libs.bundles.kotlinx.coroutines)
                implementation(libs.coil.compose)
                implementation(libs.coil.test)
                implementation(libs.test.parameter.injector)
                implementation(libs.koin.test)
            }
        }
    }

    // Copied from https://github.com/chrisbanes/tivi/pull/1827/commits/0840a6c769c8b91f520e03c5a2fa6292431a99ea
    targets.configureEach {
        val isAndroidTarget = platformType == KotlinPlatformType.androidJvm
        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    if (isAndroidTarget) {
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
    add("kspJvm", libs.koin.ksp.compiler)
}
