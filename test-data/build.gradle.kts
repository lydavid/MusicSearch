plugins {
    id("ly.david.android.library")
    kotlin("android")
}

android {
    namespace = "ly.david.data.test"
}

dependencies {
    implementation(projects.data)
}
