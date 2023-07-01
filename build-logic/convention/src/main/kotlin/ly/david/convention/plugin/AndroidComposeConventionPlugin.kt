package ly.david.convention.plugin

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            android {
                buildFeatures {
                    compose = true
                }

                composeOptions {
                    // TODO: get from toml
                    kotlinCompilerExtensionVersion = "1.4.8"
                }
            }
        }
    }
}

private fun Project.android(configure: CommonExtension<*, *, *, *>.() -> Unit) =
    extensions.configure(CommonExtension::class, configure)
