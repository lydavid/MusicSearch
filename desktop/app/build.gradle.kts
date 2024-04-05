plugins {
    kotlin("jvm")
    id("ly.david.musicsearch.compose.multiplatform")
    application
    alias(libs.plugins.aboutlibraries)
}

group = "ly.david.musicsearch"
version = project.properties["VERSION_NAME"] as String

application {
    mainClass.set("MainKt")
}

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

    // gradle desktop:app:projectHealth falsely reports this as unused
    implementation(compose.desktop.currentOs)

    implementation(libs.circuit.foundation)
    implementation(libs.koin.core)
}

distributions {
    main {
        distributionBaseName = "MusicSearch"
    }
}
