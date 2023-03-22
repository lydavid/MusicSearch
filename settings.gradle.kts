pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

// https://docs.gradle.org/current/userguide/platforms.html
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {

            version("accompanist", "0.28.0")
            version("compose-foundation", "1.3.1")
            version("compose-material", "1.4.0")
            version("compose-material3", "1.1.0-alpha07")
            version("compose-ui", "1.3.3")
            version("hilt", "2.45")
            version("hilt-navigation", "1.0.0")
            version("junit", "4.13.2")
            version("mockk", "1.13.4")
            version("moshi", "1.14.0")
            version("retrofit", "2.9.0")
            version("room", "2.5.0")

            library("accompanist-swiperefresh", "com.google.accompanist", "accompanist-swiperefresh").versionRef("accompanist")
            library("accompanist-pager", "com.google.accompanist", "accompanist-pager").versionRef("accompanist")

            // https://developer.android.com/jetpack/androidx/releases/compose
            library("compose-foundation", "androidx.compose.foundation", "foundation").versionRef("compose-foundation")
            library("compose-material-icons-extended", "androidx.compose.material", "material-icons-extended").versionRef("compose-material")
            library("compose-material3", "androidx.compose.material3", "material3").versionRef("compose-material3")
            library("compose-ui", "androidx.compose.ui", "ui").versionRef("compose-ui")
            library("compose-ui-preview", "androidx.compose.ui", "ui-tooling-preview").versionRef("compose-ui")
            library("compose-ui-test", "androidx.compose.ui", "ui-test-junit4").versionRef("compose-ui")
            library("compose-ui-tooling", "androidx.compose.ui", "ui-tooling").versionRef("compose-ui")

            library("dagger", "com.google.dagger", "dagger").versionRef("hilt")
            library("hilt-android", "com.google.dagger", "hilt-android").versionRef("hilt")
            library("hilt-android-compiler", "com.google.dagger", "hilt-android-compiler").versionRef("hilt")
            library("hilt-android-testing", "com.google.dagger", "hilt-android-testing").versionRef("hilt")
            library("hilt-navigation-compose", "androidx.hilt", "hilt-navigation-compose").versionRef("hilt-navigation")

            library("junit", "junit", "junit").versionRef("junit")

            library("mockk", "io.mockk", "mockk").versionRef("mockk")

            library("moshi-kotlin", "com.squareup.moshi", "moshi-kotlin").versionRef("moshi")

            library("retrofit", "com.squareup.retrofit2", "retrofit").versionRef("retrofit")
            library("retrofit-converter-moshi", "com.squareup.retrofit2", "converter-moshi").versionRef("retrofit")

            library("room-common", "androidx.room", "room-common").versionRef("room")
            library("room-compiler", "androidx.room", "room-compiler").versionRef("room")
            library("room-ktx", "androidx.room", "room-ktx").versionRef("room")
            library("room-paging", "androidx.room", "room-paging").versionRef("room")
            library("room-runtime", "androidx.room", "room-runtime").versionRef("room")
            library("room-testing", "androidx.room", "room-testing").versionRef("room")
        }
    }
}

rootProject.name = "MusicSearch"

include(
    ":app",
    ":data-android",
    ":data",
    ":data:base",
    ":data:coverart",
    ":data:musicbrainz",
    ":test-data"
)
