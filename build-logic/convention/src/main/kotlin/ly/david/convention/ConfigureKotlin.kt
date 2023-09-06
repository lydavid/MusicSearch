package ly.david.convention

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure

private const val JAVA_VERSION = 17

fun Project.configureKotlin() {
    with(pluginManager) {
        apply("kotlin")
    }

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(JAVA_VERSION))
        }
    }
}
