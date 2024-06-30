package ly.david.convention.plugin

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            if (pluginManager.hasPlugin("ly.david.android.library")) {
                android {
                    buildFeatures {
                        compose = true
                    }
                }
            }
        }
    }
}

private fun Project.android(configure: CommonExtension<*, *, *, *, *, *>.() -> Unit) =
    extensions.configure(
        CommonExtension::class,
        configure,
    )
