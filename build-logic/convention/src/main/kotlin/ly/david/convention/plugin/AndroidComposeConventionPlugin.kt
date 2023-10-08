package ly.david.convention.plugin

import com.android.build.api.dsl.CommonExtension
import ly.david.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Applicable to both android applications and libraries.
 */
@Suppress("unused")
class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            android {
                buildFeatures {
                    compose = true
                }

                composeOptions {
                    kotlinCompilerExtensionVersion = libs.findVersion("compose-compiler").get().toString()
                }
            }
        }
    }
}

private fun Project.android(configure: CommonExtension<*, *, *, *, *>.() -> Unit) =
    extensions.configure(CommonExtension::class, configure)
