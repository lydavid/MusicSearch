plugins {
    kotlin("jvm")
    id("ly.david.musicsearch.compose.multiplatform")
    application
    alias(libs.plugins.aboutlibraries)
}

group = "ly.david.musicsearch"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(projects.shared)

    // TODO: eventually, the only dependent project should be shared
    //  where shared exposes the root entry point
    implementation(projects.shared.screens)
    implementation(projects.ui.core)
    implementation(projects.domain)
    implementation(projects.strings)
    implementation(projects.core.models)
    implementation(projects.core.preferences)
    implementation(projects.data.musicbrainz)

    implementation(compose.desktop.currentOs)

    implementation(libs.circuit.foundation)
    implementation(libs.circuit.overlay)
    implementation(libs.koin.core)
}

application {
    mainClass.set("MainKt")
}
