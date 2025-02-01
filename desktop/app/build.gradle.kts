plugins {
    kotlin("jvm")
    id("ly.david.musicsearch.compose.multiplatform")
    application
    alias(libs.plugins.aboutlibraries)
    id("dev.hydraulic.conveyor") version "1.12"
}

application {
    mainClass.set("MainKt")
}

group = "io.github.lydavid.musicsearch"
version = getDesktopVersion()

aboutLibraries {
    excludeFields = arrayOf("generated")
}

dependencies {
    implementation(projects.shared)

    // TODO: eventually, the only dependent project should be shared
    //  where shared exposes the root entry point
    implementation(projects.ui.common)

    linuxAmd64(compose.desktop.linux_x64)
    macAmd64(compose.desktop.macos_x64)
    macAarch64(compose.desktop.macos_arm64)
    windowsAmd64(compose.desktop.windows_x64)

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

/**
 * Transform a version name to the format MAJOR.MINOR.PATCH, where each part is a number.
 *
 * e.g. 1.2.1-beta.14 to 1.2.114
 */
fun getDesktopVersion(): String {
    var versionName = project.properties["VERSION_NAME"] as String
    val parts = versionName.split("-")
    val versionPart = parts.first()
    val betaPart = parts.last()
    if (versionPart != betaPart) {
        val splitVersion = versionPart.split(".")
        val betaNum = betaPart.split(".").last().toInt()
        val modifiedPatch = splitVersion.last().toInt() * 100 + betaNum
        versionName = "${splitVersion[0]}.${splitVersion[1]}.$modifiedPatch"
    }
    return versionName
}
