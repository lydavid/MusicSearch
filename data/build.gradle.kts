plugins {
    id("kotlin")
}

dependencies {
    api(projects.data.base)
    api(projects.data.coverart)
    api(projects.data.room)
    api(projects.data.musicbrainz)

    implementation(libs.androidx.paging.common)

    implementation(libs.dagger)

    implementation(libs.moshi.kotlin)

    implementation(libs.bundles.retrofit)

    implementation(libs.room.common)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
