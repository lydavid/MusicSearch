package ly.david.convention.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

@Suppress("unused")
class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            if (pluginManager.hasPlugin("ly.david.android.library")) {
//                configureAndroid()
//                android {
//                    buildFeatures {
//                        compose = true
//                    }
//                }
            }
            composeCompiler {
                stabilityConfigurationFiles.add(rootProject.layout.projectDirectory.file("stability_config.conf"))
            }
        }
    }
}

//private fun Project.android(configure: LibraryExtension.() -> Unit) =
//    extensions.configure(
//        LibraryExtension::class,
//        configure,
//    )

fun Project.composeCompiler(block: ComposeCompilerGradlePluginExtension.() -> Unit) {
    extensions.configure<ComposeCompilerGradlePluginExtension>(block)
}
