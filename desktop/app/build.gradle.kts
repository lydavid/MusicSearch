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
    implementation("com.github.scribejava:scribejava-apis:6.4.1")
}

application {
    mainClass.set("MainKt")
}
