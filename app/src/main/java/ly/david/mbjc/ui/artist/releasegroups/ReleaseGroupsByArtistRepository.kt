package ly.david.mbjc.ui.artist.releasegroups

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Artist
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.artist.ArtistDao
import ly.david.mbjc.data.persistence.artist.toArtistRoomModel
import ly.david.mbjc.data.persistence.history.LookupHistory
import ly.david.mbjc.data.persistence.history.LookupHistoryDao

@Singleton
internal class ReleaseGroupsByArtistRepository @Inject constructor(
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
     * Side-effect: Records a visit to this artist's page.
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
                resource = MusicBrainzResource.ARTIST,
                mbid = artist.id
            )
        )
    }
}
