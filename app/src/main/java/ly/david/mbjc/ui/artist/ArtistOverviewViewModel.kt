package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Artist
import ly.david.mbjc.data.UiArtist
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.ArtistDao
import ly.david.mbjc.data.persistence.LookupHistory
import ly.david.mbjc.data.persistence.LookupHistoryDao
import ly.david.mbjc.data.persistence.toRoomArtist
import ly.david.mbjc.data.toUiArtist
import ly.david.mbjc.ui.Destination

@Singleton
class ArtistRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val artistDao: ArtistDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var artist: UiArtist? = null

    suspend fun lookupArtist(artistId: String): UiArtist =
        artist ?: run {

            val roomArtist = artistDao.getArtist(artistId)
            if (roomArtist != null) {
                incrementOrInsertLookupHistory(roomArtist)
                return roomArtist.toUiArtist()
            }

            val musicBrainzArtist = musicBrainzApiService.lookupArtist(artistId)
            artistDao.insert(musicBrainzArtist.toRoomArtist())
            incrementOrInsertLookupHistory(musicBrainzArtist)
            musicBrainzArtist.toUiArtist()
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

@HiltViewModel
class ArtistOverviewViewModel @Inject constructor(
    private val artistRepository: ArtistRepository
) : ViewModel() {

    suspend fun lookupArtist(artistId: String): UiArtist =
        artistRepository.lookupArtist(artistId)

//    private var artist: MusicBrainzArtist? = null
//
//    suspend fun lookupArtist(artistId: String): MusicBrainzArtist =
//        artist ?: musicBrainzApiService.lookupArtist(artistId).also {
//            artistDao.insert(it)
//            incrementOrInsertLookupHistory(it)
//            artist = it
//        }
//
//    // TODO: see if we can generalize
//    private suspend fun incrementOrInsertLookupHistory(artist: MusicBrainzArtist) {
//        lookupHistoryDao.incrementOrInsertLookupHistory(
//            LookupHistory(
//                summary = artist.getNameWithDisambiguation(),
//                destination = Destination.LOOKUP_ARTIST,
//                mbid = artist.id
//            )
//        )
//    }
}
