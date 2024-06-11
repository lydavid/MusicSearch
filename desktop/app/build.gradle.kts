plugins {
    kotlin("jvm")
    id("ly.david.musicsearch.compose.multiplatform")
    application
    alias(libs.plugins.aboutlibraries)
    id("dev.hydraulic.conveyor") version "1.5"
}

application {
    mainClass.set("MainKt")
}

group = "io.github.lydavid.musicsearch"
version = "1.2.0" // project.properties["VERSION_NAME"] as String
// TODO: transform VERSION_NAME to an appropriate version for other platforms
//  need to merge -beta.13 into the patch version

aboutLibraries {
    excludeFields = arrayOf("generated")
}

dependencies {
    implementation(projects.shared)

    // TODO: eventually, the only dependent project should be shared
    //  where shared exposes the root entry point
    implementation(projects.ui.common)
    implementation(projects.ui.core)
    implementation(projects.core.preferences)

    linuxAmd64(compose.desktop.linux_x64)
    macAmd64(compose.desktop.macos_x64)
    macAarch64(compose.desktop.macos_arm64)
    windowsAmd64(compose.desktop.windows_x64)

    implementation(libs.circuit.foundation)
    implementation(libs.koin.core)
}

// region Work around temporary Compose bugs.
configurations.all {
    attributes {
        // https://github.com/JetBrains/compose-jb/issues/1404#issuecomment-1146894731
        attribute(Attribute.of("ui", String::class.java), "awt")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(19))
    }
}
