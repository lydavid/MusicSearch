plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "1.5.1"
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

compose {
    kotlinCompilerPlugin.set("1.5.0")
    kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=1.9.10")
}
