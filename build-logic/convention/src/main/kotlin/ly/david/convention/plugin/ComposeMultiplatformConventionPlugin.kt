package ly.david.convention.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension

@Suppress("unused")
class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
            }
        }
    }
}

private fun Project.compose(configure: ComposeExtension.() -> Unit) = extensions.configure(configure)
