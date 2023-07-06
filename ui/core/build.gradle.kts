plugins {
    id("ly.david.android.library")
    id("ly.david.android.compose")
}

android {
    namespace = "ly.david.ui.core"
}

dependencies {
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.preview)
    debugImplementation(libs.compose.ui.tooling)
}
