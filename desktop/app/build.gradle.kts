plugins {
    kotlin("jvm")
    id("ly.david.musicsearch.compose.multiplatform")
    application
}

group = "ly.david.musicsearch"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(projects.shared)

    // TODO: eventually, the only dependent project should be shared
    //  where shared exposes the root entry point
    implementation(projects.ui.core)
    implementation(projects.domain)
    implementation(projects.strings)
    implementation(projects.shared.feature.search)
    implementation(projects.core.models)
    implementation(projects.core.preferences)
    implementation(projects.data.musicbrainz)

    implementation(compose.desktop.currentOs)

    implementation(libs.circuit.foundation)
    implementation(libs.koin.core)
    implementation(libs.scribejava)
}

application {
    mainClass.set("MainKt")
}
