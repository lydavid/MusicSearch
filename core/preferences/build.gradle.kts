plugins {
    id("ly.david.android.library")
    kotlin("android")
}

android {
    namespace = "ly.david.musicsearch.core.preferences"
}

dependencies {
    implementation(projects.core.models)
    implementation(projects.data.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.koin.core)
}
