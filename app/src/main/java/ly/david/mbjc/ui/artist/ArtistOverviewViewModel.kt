package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.Artist
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.ArtistDao
import ly.david.mbjc.data.persistence.LookupHistory
import ly.david.mbjc.data.persistence.LookupHistoryDao
import ly.david.mbjc.ui.Destination

@HiltViewModel
class ArtistOverviewViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val artistDao: ArtistDao,
    private val lookupHistoryDao: LookupHistoryDao
) : ViewModel() {

    private var artist: Artist? = null

    suspend fun lookupArtist(artistId: String): Artist =
        artist ?: musicBrainzApiService.lookupArtist(artistId).also {
            artistDao.insert(it)
            incrementOrInsertLookupHistory(it)
            artist = it
        }

    // TODO: see if we can generalize
    private suspend fun incrementOrInsertLookupHistory(artist: Artist) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                summary = artist.getNameWithDisambiguation(),
                destination = Destination.LOOKUP_ARTIST,
                mbid = artist.id
            )
        )
    }
}
