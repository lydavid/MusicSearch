package ly.david.musicbrainzjetpackcompose.ui.artist

import androidx.lifecycle.ViewModel
import ly.david.musicbrainzjetpackcompose.musicbrainz.MusicBrainzApiService
import ly.david.musicbrainzjetpackcompose.musicbrainz.ReleaseGroups

class ArtistViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    suspend fun getReleaseGroupsByArtist(artistId: String): ReleaseGroups =
        musicBrainzApiService.getReleaseGroupsByArtist(artistId)
}
