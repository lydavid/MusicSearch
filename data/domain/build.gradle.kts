plugins {
    id("ly.david.kotlin")
}

dependencies {
    implementation(projects.data.core)
    implementation(projects.data.common.network)
    implementation(projects.data.coverart)
    implementation(projects.data.musicbrainz)
    implementation(projects.data.room)

    implementation(libs.androidx.paging.common)
    implementation(libs.dagger)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
