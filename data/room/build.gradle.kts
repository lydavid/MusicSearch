plugins {
    id("ly.david.kotlin")
}

dependencies {
    implementation(projects.data.core)
    implementation(projects.data.coverart)
    implementation(projects.data.musicbrainz)

    implementation(libs.androidx.paging.common)
    implementation(libs.room.common)
}
