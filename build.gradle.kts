
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.BasePlugin
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    id("com.android.application") version "7.4.2" apply false
    id("com.android.library") version "8.0.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.45" apply false
    id("io.gitlab.arturbosch.detekt") version "1.22.0" apply true
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.firebase.crashlytics") version "2.9.5" apply false
    id("dev.iurysouza.modulegraph") version "0.3.0"
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    repositories {
        google()
        mavenCentral()
    }

    plugins.withType<JavaBasePlugin>().configureEach {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(11))
            }
        }
    }

    plugins.withType<BasePlugin>().configureEach {
        extensions.configure<BaseExtension> {
            compileSdkVersion(33)
            defaultConfig {
                minSdk = 23
                targetSdk = 33
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
        }
        dependencies {
            detektPlugins("io.nlopez.compose.rules:detekt:0.1.5")
        }
    }

    // ./gradlew assembleRelease -Pmbjc.enableComposeCompilerReports=true --rerun-tasks
    tasks.withType<KotlinCompilationTask<*>>().configureEach {
        compilerOptions {
            freeCompilerArgs.addAll("-opt-in=kotlin.RequiresOptIn")

            if (project.findProperty("mbjc.enableComposeCompilerReports") == "true") {
                freeCompilerArgs.addAll(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                        project.buildDir.absolutePath + "/compose_metrics"
                )
                freeCompilerArgs.addAll(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                        project.buildDir.absolutePath + "/compose_metrics"
                )
            }
        }
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

tasks.register<DefaultTask>("publish") {
    group = "publish"
    description = "Dummy task so that we pass gradle-semantic-release-plugin's verifyConditions. Otherwise we would need to publish to an artifact repository."
}

moduleGraphConfig {
    readmePath.set(file("$projectDir/README.md").absolutePath)
    heading.set("## Dependency Diagram")
}
