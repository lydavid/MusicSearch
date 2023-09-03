plugins {
    id("ly.david.android.library")
}

android {
    namespace = "ly.david.data.test"
}

dependencies {
    implementation(projects.data)
}
