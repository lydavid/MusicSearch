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

            version("accompanist", "0.30.1")
            version("coil", "2.3.0")
            version("compose-compiler", "1.4.7")
            version("compose-foundation", "1.4.3")
            version("compose-material", "1.4.3")
            version("compose-material3", "1.1.0")
            version("compose-ui", "1.4.3")
            version("hilt", "2.46.1")
            version("hilt-navigation", "1.0.0")
            version("junit", "4.13.2")
            version("kotlinx-coroutines", "1.6.4")
            version("mockk", "1.13.5")
            version("moshi", "1.15.0")
            version("okhttp", "4.11.0")
            version("retrofit", "2.9.0")
            version("room", "2.5.1")

            library("accompanist-swiperefresh", "com.google.accompanist", "accompanist-swiperefresh").versionRef("accompanist")
            library("accompanist-pager", "com.google.accompanist", "accompanist-pager").versionRef("accompanist")

            library("androidx-activity-compose", "androidx.activity", "activity-compose").version("1.7.1")
            library("androidx-arch-core-testing", "androidx.arch.core", "core-testing").version("2.2.0")
            library("androidx-constraintlayout-compose", "androidx.constraintlayout", "constraintlayout-compose").version("1.0.1")
            library("androidx-core", "androidx.core", "core-ktx").version("1.10.1")
            library("androidx-datastore-preferences", "androidx.datastore", "datastore-preferences").version("1.0.0")
            library("androidx-lifecycle-viewmodel-compose", "androidx.lifecycle", "lifecycle-viewmodel-compose").version("2.6.1")
            library("androidx-navigation-compose", "androidx.navigation", "navigation-compose").version("2.5.3")
            library("androidx-paging-compose", "androidx.paging", "paging-compose").version("1.0.0-alpha14")
            library("androidx-paging-runtime", "androidx.paging", "paging-runtime-ktx").version("3.1.1")
            library("androidx-test-junit", "androidx.test.ext", "junit").version("1.1.5")
            library("androidx-test-espresso-core", "androidx.test.espresso", "espresso-core").version("3.5.1")

            library("appauth", "net.openid", "appauth").version("0.11.1")

            library("coil-base", "io.coil-kt", "coil-base").versionRef("coil")
            library("coil-compose", "io.coil-kt", "coil-compose").versionRef("coil")
            library("coil-test", "io.coil-kt", "coil-test").versionRef("coil")

            // https://developer.android.com/jetpack/androidx/releases/compose
            library("compose-compiler", "androidx.compose.compiler", "compiler").versionRef("compose-compiler")
            library("compose-foundation", "androidx.compose.foundation", "foundation").versionRef("compose-foundation")
            library("compose-material-icons-extended", "androidx.compose.material", "material-icons-extended").versionRef("compose-material")
            library("compose-material3", "androidx.compose.material3", "material3").versionRef("compose-material3")
            library("compose-ui", "androidx.compose.ui", "ui").versionRef("compose-ui")
            library("compose-ui-preview", "androidx.compose.ui", "ui-tooling-preview").versionRef("compose-ui")
            library("compose-ui-test", "androidx.compose.ui", "ui-test-junit4").versionRef("compose-ui")
            library("compose-ui-tooling", "androidx.compose.ui", "ui-tooling").versionRef("compose-ui")

            library("dagger", "com.google.dagger", "dagger").versionRef("hilt")

            library("firebase-bom", "com.google.firebase", "firebase-bom").version("32.0.0")
            library("firebase-analytics", "com.google.firebase", "firebase-analytics-ktx").version("21.2.2")
            library("firebase-crashlytics", "com.google.firebase", "firebase-crashlytics-ktx").version("18.3.7")

            library("hilt-android", "com.google.dagger", "hilt-android").versionRef("hilt")
            library("hilt-android-compiler", "com.google.dagger", "hilt-android-compiler").versionRef("hilt")
            library("hilt-android-testing", "com.google.dagger", "hilt-android-testing").versionRef("hilt")
            library("hilt-navigation-compose", "androidx.hilt", "hilt-navigation-compose").versionRef("hilt-navigation")

            library("junit", "junit", "junit").versionRef("junit")

            library("kotlinx-coroutines-android", "org.jetbrains.kotlinx", "kotlinx-coroutines-android").versionRef("kotlinx-coroutines")
            library("kotlinx-coroutines-test", "org.jetbrains.kotlinx", "kotlinx-coroutines-test").versionRef("kotlinx-coroutines")
            bundle("kotlinx-coroutines", listOf("kotlinx-coroutines-android", "kotlinx-coroutines-test"))

            library("mockk", "io.mockk", "mockk").versionRef("mockk")

            library("moshi-kotlin", "com.squareup.moshi", "moshi-kotlin").versionRef("moshi")

            library("okhttp-mockwebserver", "com.squareup.okhttp3", "mockwebserver").versionRef("okhttp")
            library("okhttp-tls", "com.squareup.okhttp3", "okhttp-tls").versionRef("okhttp")

            library("retrofit", "com.squareup.retrofit2", "retrofit").versionRef("retrofit")
            library("retrofit-converter-moshi", "com.squareup.retrofit2", "converter-moshi").versionRef("retrofit")
            bundle("retrofit", listOf("retrofit", "retrofit-converter-moshi"))

            library("room-common", "androidx.room", "room-common").versionRef("room")
            library("room-compiler", "androidx.room", "room-compiler").versionRef("room")
            library("room-ktx", "androidx.room", "room-ktx").versionRef("room")
            library("room-paging", "androidx.room", "room-paging").versionRef("room")
            library("room-runtime", "androidx.room", "room-runtime").versionRef("room")
            library("room-testing", "androidx.room", "room-testing").versionRef("room")

            library("timber", "com.jakewharton.timber", "timber").version("5.0.1")
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
