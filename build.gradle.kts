import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import java.io.BufferedReader

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.baselineprofile) apply false
    alias(libs.plugins.compose.compiler) apply false
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
    alias(libs.plugins.kotlin.powerAssert) apply false

    alias(libs.plugins.aboutlibraries)
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.gradle.versions)
    alias(libs.plugins.version.catalog.update)
}

buildscript {
    dependencies {
        // Workaround for CMP and buildconfig mismatch: https://github.com/gmazzo/gradle-buildconfig-plugin/issues/131
        classpath("com.squareup:kotlinpoet:2.1.0")
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
project.logger.log(
    LogLevel.DEBUG,
    "Java ${System.getProperty("java.version")} ${System.getProperty("java.vendor")}",
)
project.logger.log(
    LogLevel.DEBUG,
    "Gradle ${gradle.gradleVersion} at ${gradle.gradleHomeDir}",
)

// Android Studio highlighting in standalone gradle.kts scripts seems to be broken:
// https://issuetracker.google.com/issues/293048764
// So for now, we will just put them all under here:

// region Project tasks
private val projectGroup = "MusicSearch"

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

    val dependenciesGraphRelativePath = "assets/module_dependency_graph.svg"
    val dependenciesGraphAbsolutePath = "$projectDir/$dependenciesGraphRelativePath"

    val dependentsGraphRelativePath = "assets/module_dependent_graph.svg"
    val dependentsGraphAbsolutePath = "$projectDir/$dependentsGraphRelativePath"

    fun getGraphvizTraits(currentProject: Project): String {
        val traits = mutableListOf<String>()

        if (currentProject == project) {
            traits.add("shape=box")
        }

        if (currentProject.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
            traits.add("fillcolor=\"#b59aff\"")
        } else if (currentProject.plugins.hasPlugin("org.jetbrains.kotlin.js")) {
            traits.add("fillcolor=\"#ffe89a\"")
        } else if (currentProject.plugins.hasPlugin("com.android.library") ||
            currentProject.plugins.hasPlugin("com.android.application")
        ) {
            traits.add("fillcolor=\"#9affb5\"")
        } else if (currentProject.plugins.hasPlugin("java-library") ||
            currentProject.plugins.hasPlugin("java")
        ) {
            traits.add("fillcolor=\"#9affb5\"")
        } else {
            traits.add("fillcolor=\"#eeeeee\"")
        }

        return "[${traits.joinToString(", ")}]"
    }

    tasks.register("projectDependenciesGraph") {
        group = projectGroup

        inputs.files(
            fileTree(rootDir) {
                include("**/build.gradle.kts")
            },
        )

        outputs.file(dependenciesGraphAbsolutePath)

        doLast {
            val dot = project.layout.buildDirectory.file("reports/dependency-graph/project.dot").get().asFile
            dot.parentFile.mkdirs()
            dot.delete()

            dot.writeText("digraph {\n")
            dot.appendText("  graph [label=\"Dependencies\\n \",labelloc=t,fontsize=30,ranksep=1.4];\n")
            dot.appendText("  node [style=filled, fillcolor=\"#bbbbbb\"];\n")
            dot.appendText("  rankdir=TB;\n")
            dot.appendText("  splines=ortho;\n")

            val projects = LinkedHashSet<Project>()

            // Find all projects in this repository and group them accordingly
            var queue = mutableListOf(rootProject)
            while (queue.isNotEmpty()) {
                val currentProject = queue.removeFirst()

                // A child project is not necessarily a dependency
                // eg. :feature:collections is child project of :shared
                queue.addAll(currentProject.childProjects.values)
            }

            val dependencies = LinkedHashMap<Pair<Project, Project>, List<String>>()
            val visitedProjects = mutableSetOf<Project>()
            queue = mutableListOf(project)
            while (queue.isNotEmpty()) {
                val currentProject = queue.removeFirst()

                if (!visitedProjects.add(currentProject)) {
                    continue
                }

                currentProject.configurations.forEach outer@{ config ->
                    config.dependencies
                        .withType(ProjectDependency::class.java)
                        .map { it.dependencyProject }
                        .filter { currentProject != rootProject }
                        .filter { currentProject != it }
                        .filter { dependency -> dependency.path != ":android:baselineprofile" }
                        .forEach inner@{ dependency ->

                            projects.add(currentProject)
                            projects.add(dependency)
                            queue.add(dependency)

                            val traits = mutableListOf<String>()
                            if (config.name.lowercase().endsWith("implementation")) {
                                traits.add("style=dotted")
                            }
                            if (config.name.lowercase().contains("test")) {
                                traits.add("color=\"#ff9ab1\"")
                            }
                            dependencies[currentProject to dependency] = traits
                        }
                }
            }

            // Don't create an svg for projects with no dependencies
            if (dependencies.isEmpty()) {
                file(dependenciesGraphAbsolutePath).delete()
                return@doLast
            }

            projects.sortedBy { it.path }

            dot.appendText("\n  # Projects\n\n")
            for (currentProject in projects) {
                dot.appendText("  \"${currentProject.path}\" ${getGraphvizTraits(currentProject)};\n")
            }

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
                    "dot",
                    "-Tsvg",
                    "project.dot",
                    "-o",
                    dependenciesGraphAbsolutePath,
                ),
                emptyArray(),
                dot.parentFile,
            )
            p.waitFor()
            require(p.exitValue() == 0) { p.errorStream.bufferedReader().use(BufferedReader::readText) }

            println("Generated $dependenciesGraphAbsolutePath")
        }
    }

    tasks.register("projectDependentsGraph") {
        group = projectGroup

        inputs.files(
            fileTree(rootDir) {
                include("**/build.gradle.kts")
            },
        )

        outputs.file(dependentsGraphAbsolutePath)

        doLast {
            val dot = project.layout.buildDirectory.file("reports/dependent-graph/project.dot").get().asFile
            dot.parentFile.mkdirs()
            dot.delete()

            dot.writeText("digraph {\n")
            dot.appendText("  graph [label=\"Dependents\\n \",labelloc=t,fontsize=30,ranksep=1.4];\n")
            dot.appendText("  node [style=filled, fillcolor=\"#bbbbbb\"];\n")
            dot.appendText("  rankdir=RL;\n")
            dot.appendText("  splines=ortho;\n")

            val projects = LinkedHashSet<Project>()
            val queue = mutableListOf(rootProject)
            while (queue.isNotEmpty()) {
                val currentProject = queue.removeFirst()

                // A child project is not necessarily a dependency
                // eg. :feature:collections is child project of :shared
                queue.addAll(currentProject.childProjects.values)
                projects.addAll(currentProject.childProjects.values)
            }

            val dependents = LinkedHashMap<Pair<Project, Project>, List<String>>()
            projects.forEach { currentProject ->
                currentProject.configurations.forEach outer@{ config ->
                    config.dependencies
                        .withType(ProjectDependency::class.java)
                        .map { it.dependencyProject }
                        .filter { currentProject != rootProject }
                        .filter { currentProject != it }
                        .filter { dependency -> dependency.path != ":android:baselineprofile" }
                        .forEach inner@{ dependency ->
                            if (dependency == project) {
                                val traits = mutableListOf<String>()
                                if (config.name.lowercase().endsWith("implementation")) {
                                    traits.add("style=dotted")
                                }
                                if (config.name.lowercase().contains("test")) {
                                    traits.add("color=\"#ff9ab1\"")
                                }
                                dependents[currentProject to project] = traits
                            }
                        }
                }
            }

            if (dependents.isEmpty()) {
                file(dependentsGraphAbsolutePath).delete()
                return@doLast
            }

            dot.appendText("\n  # Projects\n\n")
            dot.appendText("  \"${project.path}\" ${getGraphvizTraits(project)};\n")
            dependents.forEach { (key, _) ->
                val currentProject = key.first
                dot.appendText("  \"${key.first.path}\" ${getGraphvizTraits(currentProject)};\n")
            }

            dot.appendText("\n  # Dependents\n\n")
            dependents.forEach { (key, traits) ->
                dot.appendText("  \"${key.first.path}\" -> \"${key.second.path}\"")
                if (traits.isNotEmpty()) {
                    dot.appendText(" [${traits.joinToString(", ")}]")
                }
                dot.appendText("\n")
            }

            dot.appendText("}\n")

            val p = Runtime.getRuntime().exec(
                arrayOf(
                    "dot",
                    "-Tsvg",
                    "project.dot",
                    "-o",
                    dependentsGraphAbsolutePath,
                ),
                emptyArray(),
                dot.parentFile,
            )
            p.waitFor()
            require(p.exitValue() == 0) { p.errorStream.bufferedReader().use(BufferedReader::readText) }

            println("Generated $dependentsGraphAbsolutePath")
        }
    }

    val generateProjectReadme = "generateProjectReadme"
    tasks.register(generateProjectReadme) {
        group = projectGroup
        description = "Creates the README.md for individual modules, not the root README.md."

        dependsOn(
            "projectDependenciesGraph",
            "projectDependentsGraph",
        )

        inputs.files(
            dependenciesGraphAbsolutePath,
            dependentsGraphAbsolutePath,
        )

        val readmeFile = file("$projectDir/README.md")
        outputs.file(readmeFile)

        doLast {
            val dependenciesGraphSvgFile = file(dependenciesGraphAbsolutePath)
            val dependentsGraphSvgFile = file(dependentsGraphAbsolutePath)
            if (!dependenciesGraphSvgFile.exists() && !dependentsGraphSvgFile.exists()) return@doLast
            readmeFile.writeText(
                """
                # ${project.path}

                Do not edit this file.
                It was automatically generated by the `$generateProjectReadme` task.
                """.trimIndent(),
            )

            if (dependenciesGraphSvgFile.exists()) {
                readmeFile.appendText(
                    """
                
                
                ## Dependencies
                ![]($dependenciesGraphRelativePath)
                    """.trimIndent(),
                )
            }

            if (dependentsGraphSvgFile.exists()) {
                readmeFile.appendText(
                    """
                
                
                ## Dependents
                ![]($dependentsGraphRelativePath)
                    """.trimIndent(),
                )
            }

            println("Generated ${readmeFile.path}")
        }
    }
}
// endregion
