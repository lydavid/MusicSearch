package ly.david.musicbrainzjetpackcompose.ui.releasegroup

import androidx.lifecycle.ViewModel
import ly.david.musicbrainzjetpackcompose.musicbrainz.BrowseReleasesResponse
import ly.david.musicbrainzjetpackcompose.musicbrainz.MusicBrainzApiService

class ReleaseGroupViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    suspend fun getReleasesByReleaseGroup(releaseGroupId: String): BrowseReleasesResponse =
        musicBrainzApiService.browseReleasesByReleaseGroup(releaseGroupId)
}
