package ly.david.mbjc.ui.artist

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Artist
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.ArtistDao
import ly.david.mbjc.data.persistence.LookupHistory
import ly.david.mbjc.data.persistence.LookupHistoryDao
import ly.david.mbjc.data.persistence.toArtistRoomModel
import ly.david.mbjc.ui.navigation.Destination

@Singleton
class ArtistRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val artistDao: ArtistDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var artist: Artist? = null

    /**
     * Retrieves [Artist] from one of:
     * 1. In-memory cache
     * 2. Local database.
     * 3. MusicBrainz server.
     *
     * Side-effect: Records a visit this this artist's page.
     */
    suspend fun lookupArtist(artistId: String): Artist =
        artist ?: run {

            val roomArtist = artistDao.getArtist(artistId)
            if (roomArtist != null) {
                incrementOrInsertLookupHistory(roomArtist)
                return roomArtist
            }

            val musicBrainzArtist = musicBrainzApiService.lookupArtist(artistId)
            artistDao.insert(musicBrainzArtist.toArtistRoomModel())
            incrementOrInsertLookupHistory(musicBrainzArtist)
            musicBrainzArtist
        }

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
