plugins {
    kotlin("jvm")
    id("ly.david.musicsearch.compose.multiplatform")
    application
    alias(libs.plugins.aboutlibraries)
    id("dev.hydraulic.conveyor") version "2.0"
}

application {
    mainClass.set("MainKt")
}

group = "io.github.lydavid.musicsearch"
version = project.properties["VERSION_NAME"] as String

dependencies {
    implementation(projects.shared)
    implementation(projects.ui.common)

    linuxAmd64(libs.desktop.jvm.linux.x64)
    macAmd64(libs.desktop.jvm.macos.x64)
    macAarch64(libs.desktop.jvm.macos.arm64)
    windowsAmd64(libs.desktop.jvm.windows.x64)

    implementation(libs.kotlinx.coroutines.swing)
    implementation(libs.circuit.foundation)
    implementation(libs.koin.core)
}

// region Work around temporary Compose bugs.
configurations.all {
    attributes {
        // https://github.com/JetBrains/compose-jb/issues/1404#issuecomment-1146894731
        attribute(
            Attribute.of(
                "ui",
                String::class.java,
            ),
            "awt",
        )
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

aboutLibraries {
    export {
        excludeFields.addAll("generated")
        outputFile = file("../../shared/feature/licenses/src/commonMain/composeResources/files/aboutlibraries.json")
        prettyPrint = true
    }
    library {
        duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
        duplicationRule = com.mikepenz.aboutlibraries.plugin.DuplicateRule.GROUP
    }
}
