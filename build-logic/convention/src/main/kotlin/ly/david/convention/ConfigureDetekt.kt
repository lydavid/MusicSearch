package ly.david.convention

import io.gitlab.arturbosch.detekt.Detekt
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
        add("detektPlugins", "io.nlopez.compose.rules:detekt:0.1.10")
    }

    detekt {
        buildUponDefaultConfig = true
        allRules = false
        parallel = true

        // Use a single config file
        config = files("${project.rootDir}/config/detekt.yml")

        // Each module has its own baseline otherwise they overwrite each other
        baseline = file("${projectDir}/config/baseline.xml")
    }

    tasks.withType<Detekt>().configureEach {
        reports {
            // observe findings in your browser with structure and code snippets
            html.required.set(true)

            // similar to the console output, contains issue signature to manually edit baseline files
            txt.required.set(true)
        }
    }
}

private fun Project.detekt(configure: DetektExtension.() -> Unit) = extensions.configure(configure)
