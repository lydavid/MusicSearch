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
    alias(libs.plugins.sqldelight) apply false

    alias(libs.plugins.aboutlibraries)
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.module.dependency.graph)
}

subprojects {
    repositories {
        google()
        mavenCentral()
    }

    tasks.withType<KotlinCompilationTask<*>>().configureEach {
        compilerOptions {
            freeCompilerArgs.addAll("-opt-in=kotlin.RequiresOptIn")

            if (project.findProperty("musicsearch.enableComposeCompilerReports") == "true") {
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

tasks.register("listKMPModules") {
    subprojects
        .filter { it.plugins.hasPlugin("ly.david.musicsearch.kotlin.multiplatform") }
        .forEach {
            println(it.path)
        }
}

// Original from https://github.com/JakeWharton/SdkSearch/blob/master/gradle/projectDependencyGraph.gradle
// kts version from https://github.com/leinardi/Forlago/blob/e47f2d8781b8a05e074bf2b761e1693b14b7a06c/build-conventions/src/main/kotlin/forlago.dependency-graph-conventions.gradle.kts
tasks.register("projectDependencyGraph") {
    doLast {
        val dot = File(rootProject.buildDir, "reports/dependency-graph/project.dot")
        dot.parentFile.mkdirs()
        dot.delete()

        dot.writeText("digraph {\n")
        dot.appendText("  graph [label=\"${rootProject.name}\\n \",labelloc=t,fontsize=30,ranksep=1.4];\n")
        dot.appendText("  node [style=filled, fillcolor=\"#bbbbbb\"];\n")
        dot.appendText("  rankdir=TB;\n")

        val projects = LinkedHashSet<Project>()
        val dependencies = LinkedHashMap<Pair<Project, Project>, List<String>>()
        val multiplatformProjects = mutableListOf<Project>()
        val jsProjects = mutableListOf<Project>()
        val androidProjects = mutableListOf<Project>()
        val javaProjects = mutableListOf<Project>()

        val rootProjects = mutableListOf<Project>()
        val queue = mutableListOf<Project>(rootProject)
        while (queue.isNotEmpty()) {
            val project = queue.removeFirst()
            queue.addAll(project.childProjects.values)

            if (project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
                multiplatformProjects.add(project)
            }
            if (project.plugins.hasPlugin("org.jetbrains.kotlin.js")) {
                jsProjects.add(project)
            }
            if (project.plugins.hasPlugin("com.android.library") || project.plugins.hasPlugin("com.android.application")) {
                androidProjects.add(project)
            }
            if (project.plugins.hasPlugin("java-library") || project.plugins.hasPlugin("java")) {
                javaProjects.add(project)
            }

            project.configurations.forEach outer@{ config ->
                config.dependencies
                    .withType(ProjectDependency::class.java)
                    .map { it.dependencyProject }
                    .forEach inner@{ dependency ->
                        if (project == rootProject) return@outer
                        if (project == dependency) return@inner

                        projects.add(project)
                        projects.add(dependency)

                        if (project.path.contains("app")) {
                            rootProjects.add(project)
                        }

                        if (config.name.lowercase().endsWith("implementation")) {
                            dependencies.put(project to dependency, listOf("style=dotted"))
                        } else {
                            dependencies.put(project to dependency, listOf())
                        }
                    }
            }
        }

        projects.sortedBy { it.path }

//        println("\nAll projects")
//        projects.forEach { project ->
//            println(project)
//        }
//        println("\nDependencies")
//        for ((key, value) in dependencies) {
//            println("$key $value")
//        }

        dot.appendText("\n  # Projects\n\n")
        for (project in projects) {
            val traits = mutableListOf<String>()

            if (rootProjects.contains(project)) {
                traits.add("shape=box")
            }

            if (multiplatformProjects.contains(project)) {
                traits.add("fillcolor=\"#ffd2b3\"")
            } else if (jsProjects.contains(project)) {
                traits.add("fillcolor=\"#ffffba\"")
            } else if (androidProjects.contains(project)) {
                traits.add("fillcolor=\"#baffc9\"")
            } else if (javaProjects.contains(project)) {
                traits.add("fillcolor=\"#ffb3ba\"")
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
        dependencies.forEach { key, traits ->
            dot.appendText("  \"${key.first.path}\" -> \"${key.second.path}\"")
            if (traits.isNotEmpty()) {
                dot.appendText(" [${traits.joinToString(", ")}]")
            }
            dot.appendText("\n")
        }

        dot.appendText("}\n")

        val p = Runtime.getRuntime().exec(
            arrayOf("dot", "-Tsvg", "project.dot", "-o", "$rootDir/assets/module_dependency_graph.svg"),
            emptyArray(),
            dot.parentFile
        )
        p.waitFor()
        require(p.exitValue() == 0) { p.errorStream.bufferedReader().use(java.io.BufferedReader::readText) }

        println("Project module dependency graph created at ${dot.absolutePath}.svg")
    }
}
