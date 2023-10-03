plugins {
    id("ly.david.kotlin")
}

// TODO: is this module needed? All it does is expose its submodules
dependencies {
    api(projects.data.core)
    api(projects.data.common.network)
    api(projects.data.coverart)
    api(projects.data.spotify)
    api(projects.data.musicbrainz)
}
