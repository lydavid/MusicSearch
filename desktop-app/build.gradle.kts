plugins {
    kotlin("jvm")
    id("ly.david.musicsearch.compose.multiplatform")
    application
}

group = "ly.david.musicsearch"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(compose.desktop.currentOs)
}

application {
    mainClass.set("MainKt")
}
