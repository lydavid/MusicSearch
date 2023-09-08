import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.paparazzi) apply false

    alias(libs.plugins.aboutlibraries)
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.module.dependency.graph)
}

subprojects {
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

tasks.register("testKotlinModules") {
    description = "Run unit tests on Kotlin (non-Android) modules"
    group = "verification"
    dependsOn(
        subprojects
            .filter { it.plugins.hasPlugin("ly.david.kotlin") }
            .map { "${it.path}:test" }
    )
}

tasks.register("testKotlinMultiplatformModules") {
    description = "Run JVM tests on Kotlin Multiplatform modules"
    group = "verification"
    dependsOn(
        subprojects
            .filter { it.plugins.hasPlugin("ly.david.musicsearch.kotlin.multiplatform") }
            .map { "${it.path}:jvmTest" }
    )
}
