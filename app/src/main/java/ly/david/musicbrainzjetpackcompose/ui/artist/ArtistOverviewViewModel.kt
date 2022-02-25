package ly.david.musicbrainzjetpackcompose.ui.artist

import androidx.lifecycle.ViewModel
import ly.david.musicbrainzjetpackcompose.data.Artist
import ly.david.musicbrainzjetpackcompose.data.MusicBrainzApiService

class ArtistOverviewViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    private var artist: Artist? = null

    suspend fun lookupArtist(artistId: String): Artist =
        artist ?: musicBrainzApiService.lookupArtist(artistId).also { artist = it }
}
