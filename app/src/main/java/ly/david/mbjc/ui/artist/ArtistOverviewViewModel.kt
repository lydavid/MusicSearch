package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import ly.david.mbjc.data.Artist
import ly.david.mbjc.data.MusicBrainzApiService

class ArtistOverviewViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    private var artist: Artist? = null

    suspend fun lookupArtist(artistId: String): Artist =
        artist ?: musicBrainzApiService.lookupArtist(artistId).also { artist = it }
}
