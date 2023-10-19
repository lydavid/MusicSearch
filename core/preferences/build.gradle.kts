plugins {
    id("ly.david.android.library")
    kotlin("android")
}

android {
    namespace = "ly.david.musicsearch.core.preferences"
}

dependencies {
    implementation(projects.core.coroutines)
    implementation(projects.core.models)
    implementation(libs.androidx.datastore.preferences.android)
    implementation(libs.koin.core)
}
