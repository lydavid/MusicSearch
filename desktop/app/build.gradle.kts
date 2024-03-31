plugins {
    kotlin("jvm")
    id("ly.david.musicsearch.compose.multiplatform")
    application
    alias(libs.plugins.aboutlibraries)
}

group = "ly.david.musicsearch"
version = project.properties["VERSION_NAME"] as String

aboutLibraries {
    excludeFields = arrayOf("generated")
}

dependencies {
    implementation(projects.shared)

    // TODO: eventually, the only dependent project should be shared
    //  where shared exposes the root entry point
    implementation(projects.ui.common)
    implementation(projects.ui.core)
    implementation(projects.strings)
    implementation(projects.core.preferences)

    implementation(compose.desktop.currentOs)

    implementation(libs.circuit.foundation)
    implementation(libs.circuit.overlay)
    implementation(libs.koin.core)
}

application {
    mainClass.set("MainKt")
}
