plugins {
    id("ly.david.kotlin")
}

dependencies {
    implementation(projects.data.base)
    implementation(projects.data.coverart)
    implementation(projects.data.musicbrainz)

    implementation(libs.androidx.paging.common)
    implementation(libs.room.common)
}
