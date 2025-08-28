package ly.david.convention.plugin

import ly.david.convention.configureDetekt
import ly.david.convention.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

@Suppress("unused")
class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
            }
            kotlin {
                applyDefaultHierarchyTemplate()

                jvm()
                if (pluginManager.hasPlugin("ly.david.android.library")) {
                    androidTarget()
                }
                iosArm64()
                iosSimulatorArm64()

                if (pluginManager.hasPlugin("org.jetbrains.kotlin.plugin.parcelize")) {
                    // Copied from https://github.com/slackhq/circuit/blob/e9955929fcbb2833622d74d4a738d70e14708613/samples/bottom-navigation/build.gradle.kts#L79
                    targets.configureEach {
                        if (platformType == KotlinPlatformType.androidJvm) {
                            compilations.configureEach {
                                compileTaskProvider.configure {
                                    compilerOptions {
                                        freeCompilerArgs.addAll(
                                            "-P",
                                            "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=" +
                                                "ly.david.musicsearch.shared.domain.parcelize.Parcelize",
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            configureKotlin()
            configureDetekt()
        }
    }
}

private fun Project.kotlin(configure: KotlinMultiplatformExtension.() -> Unit) = extensions.configure(configure)
