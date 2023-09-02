plugins {
    id("ly.david.android.library")
}

android {
    namespace = "ly.david.data.test"
}

dependencies {
    api(projects.data)
}
