package ly.david.convention.plugin

import ly.david.convention.configureDetekt
import ly.david.convention.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class KotlinJvmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("kotlin")
            }
            configureKotlin()
            configureDetekt()
        }
    }
}
