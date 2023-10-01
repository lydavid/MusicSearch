package ly.david.convention

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

fun Project.configureDetekt() {

    with(pluginManager) {
        apply("io.gitlab.arturbosch.detekt")
    }

    dependencies {
        add("detektPlugins", libs.findLibrary("detekt-compose").get())
        add("detektPlugins", libs.findLibrary("detekt-formatting").get())
    }

    detekt {
        autoCorrect = true
        buildUponDefaultConfig = true
        allRules = false
        parallel = true

        // Use a single config file
        config = files("${project.rootDir}/config/detekt.yml")

        // Each module has its own baseline otherwise they overwrite each other
        baseline = file("${projectDir}/config/baseline.xml")
    }

    tasks.withType<Detekt>().configureEach {
        exclude {
            it.file.absolutePath.contains("generated")
        }
        reports {
            // observe findings in your browser with structure and code snippets
            html.required.set(true)

            // similar to the console output, contains issue signature to manually edit baseline files
            txt.required.set(true)
        }
    }
    tasks.withType<DetektCreateBaselineTask>().configureEach {
        exclude {
            it.file.absolutePath.contains("generated")
        }
    }
}

private fun Project.detekt(configure: DetektExtension.() -> Unit) = extensions.configure(configure)
