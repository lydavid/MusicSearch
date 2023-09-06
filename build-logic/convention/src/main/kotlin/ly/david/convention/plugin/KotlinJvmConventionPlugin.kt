package ly.david.convention.plugin

import ly.david.convention.configureDetekt
import ly.david.convention.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinJvmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureKotlin()
            configureDetekt()
        }
    }
}
