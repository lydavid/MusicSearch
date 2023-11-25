package ly.david.convention.plugin

import ly.david.convention.configureDetekt
import ly.david.convention.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@OptIn(ExperimentalKotlinGradlePluginApi::class)
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
            }
            configureKotlin()
            configureDetekt()
        }
    }
}

private fun Project.kotlin(configure: KotlinMultiplatformExtension.() -> Unit) = extensions.configure(configure)
