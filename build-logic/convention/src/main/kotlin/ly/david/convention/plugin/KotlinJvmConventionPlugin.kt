package ly.david.convention.plugin

import ly.david.convention.configureDetekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure

private const val JAVA_VERSION = 17

class KotlinJvmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureKotlin()
            configureDetekt()
        }
    }
}

private fun Project.configureKotlin() {
    with(pluginManager) {
        apply("kotlin")
    }

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(JAVA_VERSION))
        }
    }
}
