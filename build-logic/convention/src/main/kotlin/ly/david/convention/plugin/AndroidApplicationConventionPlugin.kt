package ly.david.convention.plugin

import com.android.build.api.dsl.ApplicationExtension
import ly.david.convention.configureDetekt
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

const val COMPILE_SDK_VERSION = 36
private const val MIN_SDK_VERSION = 24

@Suppress("unused")
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            configureAndroid()
            configureDetekt()
        }
    }
}

private fun Project.configureAndroid() {
    android {
        compileSdk = COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = MIN_SDK_VERSION

            manifestPlaceholders += mapOf("appAuthRedirectScheme" to "")
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
//                isCoreLibraryDesugaringEnabled = true
        }

//            dependencies {
//                add("coreLibraryDesugaring", libs.findLibrary("desugarjdklibs").get())
//            }
    }
}

private fun Project.android(configure: ApplicationExtension.() -> Unit) = extensions.configure(configure)
