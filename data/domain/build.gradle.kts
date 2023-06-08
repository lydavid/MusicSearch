plugins {
    id("kotlin")
}

dependencies {
    implementation(projects.data.base)
    implementation(projects.data.coverart)
    implementation(projects.data.musicbrainz)
    implementation(projects.data.room)

    implementation(libs.androidx.paging.common)
    implementation(libs.dagger)
    implementation(libs.bundles.retrofit)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
