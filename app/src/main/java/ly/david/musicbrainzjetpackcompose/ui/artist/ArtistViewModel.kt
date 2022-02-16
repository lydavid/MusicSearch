package ly.david.musicbrainzjetpackcompose.ui.artist

import androidx.lifecycle.ViewModel
import ly.david.musicbrainzjetpackcompose.musicbrainz.BrowseReleaseGroupsResponse
import ly.david.musicbrainzjetpackcompose.musicbrainz.MusicBrainzApiService

class ArtistViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    suspend fun getReleaseGroupsByArtist(artistId: String): BrowseReleaseGroupsResponse =
        musicBrainzApiService.browseReleaseGroupsByArtist(artistId)
}
