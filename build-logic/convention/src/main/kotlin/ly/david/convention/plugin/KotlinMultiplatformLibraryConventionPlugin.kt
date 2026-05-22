package ly.david.convention.plugin

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.HasHostTests
import ly.david.convention.configureDetekt
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@Suppress("unused")
class KotlinMultiplatformLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.multiplatform")
            }
            kotlin {
                applyDefaultHierarchyTemplate()

                jvm()
                configureMultiplatformAndroid()
                iosArm64()
                iosSimulatorArm64()
            }

            androidComponents {
                onVariants { variant ->
                    (variant as? HasHostTests)?.hostTests?.values?.forEach { hostTest ->
                        hostTest.manifestPlaceholders.put("appAuthRedirectScheme", "")
                    }
                }
            }

            configureKotlin()
            configureDetekt()
        }
    }
}

private fun KotlinMultiplatformExtension.configureMultiplatformAndroid() {
    android {
        compileSdk = COMPILE_SDK_VERSION

        androidResources.enable = true

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
    }
}

private fun KotlinMultiplatformExtension.android(configure: KotlinMultiplatformAndroidLibraryTarget.() -> Unit) =
    extensions.configure(configure)

internal fun Project.androidComponents(block: AndroidComponentsExtension<*, *, *>.() -> Unit) {
    extensions.configure(AndroidComponentsExtension::class.java) {
        block()
    }
}

internal fun Project.kotlin(configure: KotlinMultiplatformExtension.() -> Unit) =
    extensions.configure(configure)

private fun Project.configureKotlin() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
    }
}
