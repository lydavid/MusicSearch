plugins {
    kotlin("jvm")
    id("ly.david.musicsearch.compose.multiplatform")
    application
}

group = "ly.david.musicsearch"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(projects.ui.core)
    implementation(projects.core.preferences)
    implementation(compose.desktop.currentOs)
    implementation(libs.koin.core)
}

application {
    mainClass.set("MainKt")
}
