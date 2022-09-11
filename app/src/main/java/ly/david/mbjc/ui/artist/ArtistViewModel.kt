package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.Artist
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.artist.ArtistDao
import ly.david.mbjc.data.persistence.artist.toArtistRoomModel
import ly.david.mbjc.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.ui.common.history.RecordLookupHistory

@HiltViewModel
internal class ArtistViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val artistDao: ArtistDao,
    override val lookupHistoryDao: LookupHistoryDao
) : ViewModel(), RecordLookupHistory {

    /**
     * Retrieves [Artist] from one of:
     * 1. In-memory cache
     * 2. Local database.
     * 3. MusicBrainz server.
     */
    suspend fun getArtist(artistId: String): Artist {
        val roomArtist = artistDao.getArtist(artistId)
        if (roomArtist != null) {
            return roomArtist
        }

        val musicBrainzArtist = musicBrainzApiService.lookupArtist(
            artistId = artistId,
        )
        artistDao.insert(musicBrainzArtist.toArtistRoomModel())
        return musicBrainzArtist
    }
}
