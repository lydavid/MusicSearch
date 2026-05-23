package ly.david.convention.plugin

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import ly.david.convention.COMPILE_SDK_VERSION
import ly.david.convention.configureDetekt
import ly.david.convention.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@Suppress("unused")
class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.multiplatform")
            }
            kotlin {
                applyDefaultHierarchyTemplate()

                jvm()
//                if (pluginManager.hasPlugin("ly.david.android.library")) {
//                    androidTarget()
//                    configureAndroid()
                    configure<KotlinMultiplatformAndroidLibraryTarget> {
                        compileSdk = COMPILE_SDK_VERSION
                        withHostTest {
                            isIncludeAndroidResources = true
                        }
                        // Not required at the moment.
                        // A module that needs this should enable it for itself.
                        // https://developer.android.com/kotlin/multiplatform/plugin#configure-tests
//                        withDeviceTest {
//                            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//                            execution = "HOST"
//                        }
                        androidResources.enable = true
                    }
//                }
                iosArm64()
                iosSimulatorArm64()
            }
            configureKotlin()
            configureDetekt()
        }
    }
}

internal fun Project.kotlin(configure: KotlinMultiplatformExtension.() -> Unit) = extensions.configure(configure)
