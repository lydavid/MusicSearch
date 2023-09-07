package ly.david.convention.plugin

import ly.david.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
            }
            compose {
                kotlinCompilerPlugin.set(libs.findVersion("compose-multiplatform").get().toString())
                kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=1.9.10")
            }
        }
    }
}

private fun Project.compose(configure: ComposeExtension.() -> Unit) = extensions.configure(configure)
