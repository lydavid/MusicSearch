plugins {
    id("ly.david.android.library")
}

android {
    namespace = "ly.david.data"
}

dependencies {
    api(projects.data)
}
