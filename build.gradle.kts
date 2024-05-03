import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import java.io.BufferedReader

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.paparazzi) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.sqldelight) apply false

    alias(libs.plugins.aboutlibraries)
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.gradle.versions)
    alias(libs.plugins.version.catalog.update)
}

buildscript {
    dependencies {
        // Workaround for CMP and buildconfig mismatch: https://github.com/gmazzo/gradle-buildconfig-plugin/issues/131
        classpath("com.squareup:kotlinpoet:1.16.0")
    }
}

subprojects {
    tasks.withType<KotlinCompilationTask<*>>().configureEach {
        compilerOptions {
            freeCompilerArgs.addAll("-opt-in=kotlin.RequiresOptIn")

            if (project.findProperty("musicsearch.enableComposeCompilerReports") == "true") {
                freeCompilerArgs.addAll(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                        project.layout.buildDirectory.asFile.get().absolutePath + "/compose_metrics",
                )
                freeCompilerArgs.addAll(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                        project.layout.buildDirectory.asFile.get().absolutePath + "/compose_metrics",
                )
            }
        }
    }

    // Paparazzi workaround
    plugins.withId("app.cash.paparazzi") {
        // Defer until afterEvaluate so that testImplementation is created by Android plugin.
        afterEvaluate {
            dependencies.constraints {
                add(
                    "testImplementation",
                    "com.google.guava:guava",
                ) {
                    attributes {
                        attribute(
                            TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE,
                            objects.named(
                                TargetJvmEnvironment::class.java,
                                TargetJvmEnvironment.STANDARD_JVM,
                            ),
                        )
                    }
                    because(
                        "LayoutLib and sdk-common depend on Guava's -jre published variant." +
                            "See https://github.com/cashapp/paparazzi/issues/906.",
                    )
                }
            }
        }
    }
}

// Debugging info copied from https://github.com/JFormDesigner/markdown-writer-fx/blob/62bab275f8b32ced1aae80b64ff3389c7c4f5e8c/build.gradle.kts#L30
println("Java ${System.getProperty("java.version")} ${System.getProperty("java.vendor")}")
println("Gradle ${gradle.gradleVersion} at ${gradle.gradleHomeDir}")

// Android Studio highlighting in standalone gradle.kts scripts seems to be broken:
// https://issuetracker.google.com/issues/293048764
// So for now, we will just put them all under here:

// region Project tasks
private val projectGroup = "MusicSearch"

tasks.register("testKotlinModules") {
    description = "Run unit tests on Kotlin (non-Android) modules"
    group = projectGroup
    dependsOn(
        subprojects
            .filter { it.plugins.hasPlugin("ly.david.kotlin") }
            .map { "${it.path}:test" },
    )
}

tasks.register("testKotlinMultiplatformModules") {
    description = "Run JVM tests on Kotlin Multiplatform modules"
    group = projectGroup
    dependsOn(
        subprojects
            .filter { it.plugins.hasPlugin("ly.david.musicsearch.kotlin.multiplatform") }
            .map { "${it.path}:jvmTest" },
    )
}

tasks.register("listKMPModules") {
    group = projectGroup
    subprojects
        .filter { it.plugins.hasPlugin("ly.david.musicsearch.kotlin.multiplatform") }
        .forEach {
            println(it.path)
        }
}

// Original from https://github.com/JakeWharton/SdkSearch/blob/master/gradle/projectDependencyGraph.gradle
// kts version from https://github.com/leinardi/Forlago/blob/e47f2d8781b8a05e074bf2b761e1693b14b7a06c/build-conventions/src/main/kotlin/forlago.dependency-graph-conventions.gradle.kts
allprojects {
    tasks.register("projectDependencyGraph") {
        group = projectGroup

        inputs.files(
            fileTree(rootDir) {
                include("**/build.gradle.kts")
            },
        )

        val outputFile = "$projectDir/assets/module_dependency_graph.svg"
        outputs.file(outputFile)

        doLast {
            val dot = project.layout.buildDirectory.file("reports/dependency-graph/project.dot").get().asFile
            dot.parentFile.mkdirs()
            dot.delete()

            dot.writeText("digraph {\n")
            dot.appendText("  graph [label=\"${project.name}\\n \",labelloc=t,fontsize=30,ranksep=1.4];\n")
            dot.appendText("  node [style=filled, fillcolor=\"#bbbbbb\"];\n")
            dot.appendText("  rankdir=TB;\n")
            dot.appendText("  splines=ortho;\n")

            val projects = LinkedHashSet<Project>()
            val dependencies = LinkedHashMap<Pair<Project, Project>, List<String>>()
            val multiplatformProjects = mutableListOf<Project>()
            val jsProjects = mutableListOf<Project>()
            val androidProjects = mutableListOf<Project>()
            val javaProjects = mutableListOf<Project>()

            val rootProjects = mutableListOf<Project>()
            val queue = mutableListOf(rootProject)
            while (queue.isNotEmpty()) {
                println("queue=$queue\n")
                val currentProject = queue.removeFirst()
                println("currentProject=${currentProject.path}")
                println("childProjects=${currentProject.childProjects.values}\n")

                // eg. :feature:collections is child project of :shared
                queue.addAll(currentProject.childProjects.values)

                if (currentProject.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
                    multiplatformProjects.add(currentProject)
                }
                if (currentProject.plugins.hasPlugin("org.jetbrains.kotlin.js")) {
                    jsProjects.add(currentProject)
                }
                if (currentProject.plugins.hasPlugin("com.android.library") ||
                    currentProject.plugins.hasPlugin("com.android.application")
                ) {
                    androidProjects.add(currentProject)
                }
                if (currentProject.plugins.hasPlugin("java-library") || currentProject.plugins.hasPlugin("java")) {
                    javaProjects.add(currentProject)
                }
            }

            val newQueue = mutableListOf(project)
            while (newQueue.isNotEmpty()) {
                println("newQueue=$newQueue\n")
                val currentProject = newQueue.removeFirst()
                println("currentProject=${currentProject.path}")
                println("childProjects=${currentProject.childProjects.values}\n")

                currentProject.configurations.forEach outer@{ config ->
                    config.dependencies
                        .withType(ProjectDependency::class.java)
                        .map { it.dependencyProject }
                        .filter { currentProject != rootProject }
                        .filter { currentProject != it }
                        .forEach inner@{ dependency ->

                            projects.add(currentProject)
                            projects.add(dependency)
                            newQueue.add(dependency)

                            if (currentProject == project) {
                                rootProjects.add(currentProject)
                            }

                            val traits = mutableListOf<String>()
                            if (currentProject.path.split(":").getOrNull(1)?.equals("ui") == true &&
                                dependency.path.contains("data")
                            ) {
                                traits.add("color=orange")
                            } else if (currentProject.path.split(":").getOrNull(1)?.equals("ui") == true &&
                                dependency.path.split(":").getOrNull(1)?.equals("ui") == true
                            ) {
                                traits.add("color=red")
                            } else if (config.name.lowercase().endsWith("implementation")) {
                                traits.add("style=dotted")
                            }
                            if (config.name.lowercase().contains("test")) {
                                traits.add("color=\"#ff9ab1\"")
                            }
                            dependencies[currentProject to dependency] = traits
                        }
                }
            }

            projects.sortedBy { it.path }

            println("\nAll projects")
            projects.forEach { project ->
                println(project)
            }
            println("\nDependencies")
            for ((key, value) in dependencies) {
                println("$key $value")
            }

            dot.appendText("\n  # Projects\n\n")
            for (project in projects) {
                val traits = mutableListOf<String>()

                if (rootProjects.contains(project)) {
                    traits.add("shape=box")
                    traits.add("width=5")
                }

                if (multiplatformProjects.contains(project)) {
                    traits.add("fillcolor=\"#b59aff\"")
                } else if (jsProjects.contains(project)) {
                    traits.add("fillcolor=\"#ffe89a\"")
                } else if (androidProjects.contains(project)) {
                    traits.add("fillcolor=\"#9affb5\"")
                } else if (javaProjects.contains(project)) {
                    traits.add("fillcolor=\"#ffb59a\"")
                } else {
                    traits.add("fillcolor=\"#eeeeee\"")
                }

                dot.appendText("  \"${project.path}\" [${traits.joinToString(", ")}];\n")
            }

            dot.appendText("\n  {rank = same;")
            for (project in projects) {
                if (rootProjects.contains(project)) {
                    dot.appendText(" \"${project.path}\";")
                }
            }
            dot.appendText("}\n")

            dot.appendText("\n  # Dependencies\n\n")
            dependencies.forEach { (key, traits) ->
                dot.appendText("  \"${key.first.path}\" -> \"${key.second.path}\"")
                if (traits.isNotEmpty()) {
                    dot.appendText(" [${traits.joinToString(", ")}]")
                }
                dot.appendText("\n")
            }

            dot.appendText("}\n")

            val p = Runtime.getRuntime().exec(
                arrayOf(
                    "/opt/homebrew/bin/dot", // TODO: for testing, change back
                    "-Tsvg",
                    "project.dot",
                    "-o",
                    outputFile,
                ),
                emptyArray(),
                dot.parentFile,
            )
            p.waitFor()
            require(p.exitValue() == 0) { p.errorStream.bufferedReader().use(BufferedReader::readText) }

            println("Project module dependency graph created at ${dot.absolutePath}.svg")
        }
    }
}
// endregion
