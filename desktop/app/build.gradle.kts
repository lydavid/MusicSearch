plugins {
    kotlin("jvm")
    id("ly.david.musicsearch.compose.multiplatform")
    application
}

group = "ly.david.musicsearch"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(projects.ui.core)
    implementation(projects.strings)
    implementation(projects.core.preferences)
    implementation(projects.data.musicbrainz)
    implementation(compose.desktop.currentOs)
    implementation(libs.koin.core)
    implementation(libs.scribejava)
}

application {
    mainClass.set("MainKt")
}
