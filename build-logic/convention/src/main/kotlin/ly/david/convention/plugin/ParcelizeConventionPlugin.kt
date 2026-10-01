package ly.david.convention.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

@Suppress("unused")
class ParcelizeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.parcelize")
            }
            kotlin {
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
    }
}
