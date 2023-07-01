import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    val android = "8.0.2"
    val kotlin = "1.8.22"

    id("com.android.application") version android apply false
    id("com.android.library") version android apply false
    id("org.jetbrains.kotlin.android") version kotlin apply false
    id("org.jetbrains.kotlin.jvm") version kotlin apply false
    id("com.google.dagger.hilt.android") version "2.46.1" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.0" apply true
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.firebase.crashlytics") version "2.9.6" apply false
    id("dev.iurysouza.modulegraph") version "0.4.0"
    id("app.cash.paparazzi") version "1.2.0" apply false
    id("com.mikepenz.aboutlibraries.plugin") version "10.8.0" apply true
}

subprojects {
    apply(plugin = "com.mikepenz.aboutlibraries.plugin")

    repositories {
        google()
        mavenCentral()
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
}

moduleGraphConfig {
    readmePath.set(file("$projectDir/README.md").absolutePath)
    heading.set("## Dependency Diagram")
}
