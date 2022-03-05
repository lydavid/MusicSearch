package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.Artist
import ly.david.mbjc.data.ArtistDao
import ly.david.mbjc.data.LookupHistory
import ly.david.mbjc.data.LookupHistoryDao
import ly.david.mbjc.data.MusicBrainzApiService
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.Destination

@HiltViewModel
class ArtistOverviewViewModel @Inject constructor(
    private val artistDao: ArtistDao,
    private val lookupHistoryDao: LookupHistoryDao
) : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    private var artist: Artist? = null

    suspend fun lookupArtist(artistId: String): Artist =
        artist ?: musicBrainzApiService.lookupArtist(artistId).also {
            artistDao.insert(it)
            addToHistory(it)
            artist = it
        }

    private suspend fun addToHistory(artist: Artist) {
        lookupHistoryDao.insert(
            LookupHistory(
                summary = artist.getNameWithDisambiguation(),
                destination = Destination.LOOKUP_ARTIST,
                mbid = artist.id,
                numberOfVisits = 1
            )
        )
    }
}
