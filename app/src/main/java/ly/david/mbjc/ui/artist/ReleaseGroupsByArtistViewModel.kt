package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import ly.david.mbjc.data.UiReleaseGroup
import ly.david.mbjc.data.network.BROWSE_LIMIT
import ly.david.mbjc.data.network.DELAY_PAGED_API_CALLS_MS
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.MusicBrainzReleaseGroup
import ly.david.mbjc.data.persistence.ReleaseGroupArtistDao
import ly.david.mbjc.data.persistence.ReleaseGroupDao
import ly.david.mbjc.data.persistence.RoomReleaseGroup
import ly.david.mbjc.data.persistence.RoomReleaseGroupArtistCredit
import ly.david.mbjc.data.persistence.toRoomReleaseGroup
import ly.david.mbjc.data.toUiReleaseGroup

@HiltViewModel
class ReleaseGroupsByArtistViewModel @Inject constructor(
    private val releaseGroupDao: ReleaseGroupDao,
    private val releaseGroupArtistDao: ReleaseGroupArtistDao
) : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    // TODO: Would be nice if view can listen to this
    //  and we would emit data every time one api is complete, meaning user doesn't have to wait for all api to complete
    //  to start viewing data
    //  and when new data is emitted, we don't force scroll to the top
    private val musicBrainzReleaseGroups = mutableListOf<MusicBrainzReleaseGroup>()
    private val uiReleaseGroups = mutableListOf<UiReleaseGroup>()

    // TODO: as flow?
    suspend fun getReleaseGroupsByArtist(
        artistId: String,
        limit: Int = BROWSE_LIMIT,
        offset: Int = 0
    ): List<UiReleaseGroup> {
        if (uiReleaseGroups.isNotEmpty()) {
            return uiReleaseGroups
        }

        // TODO: we could store a count of how many release groups an artist has, so that we can continue fetching if it's not complete
        //  should be in artist's table? Update it there once we get it via browse release groups by artist
        val releaseGroupsByArtistInRoom = releaseGroupDao.getNumberOfReleaseGroupsByArtist(artistId)
        uiReleaseGroups.addAll(
            if (releaseGroupsByArtistInRoom == 0) {
                getReleaseGroupsByArtistFromNetwork(artistId, limit, offset).map { it.toUiReleaseGroup() }
            } else {
                val roomReleaseGroups: List<RoomReleaseGroup> = releaseGroupDao.getAllReleaseGroupsByArtist(artistId)
                roomReleaseGroups.map { it.toUiReleaseGroup(releaseGroupArtistDao.getReleaseGroupArtistCredits(it.id)) }
            }
        )
        return uiReleaseGroups
    }

    private suspend fun getReleaseGroupsByArtistFromNetwork(
        artistId: String,
        limit: Int = BROWSE_LIMIT,
        offset: Int = 0
    ): List<MusicBrainzReleaseGroup> {
        if (offset != 0) {
            delay(DELAY_PAGED_API_CALLS_MS)
        }
        val response = musicBrainzApiService.browseReleaseGroupsByArtist(
            artistId = artistId,
            limit = limit,
            offset = offset
        )

        val newReleaseGroups = response.releaseGroups
        musicBrainzReleaseGroups.addAll(newReleaseGroups)
        return if (musicBrainzReleaseGroups.size < response.releaseGroupCount) {
            getReleaseGroupsByArtistFromNetwork(
                artistId = artistId,
                offset = offset + newReleaseGroups.size
            )
        } else {
            releaseGroupDao.insertAll(musicBrainzReleaseGroups.map { it.toRoomReleaseGroup() })

            // TODO: insert after each api call? That way, if interrupted, we will have saved data
            //  can we also start off where we left off by comparing number of entries vs releaseGroupCount?
            //  If so, provide way to invalidate database and fetch fresh, in case a new release group is added between already fetched data
            releaseGroupArtistDao.insertAll(
                musicBrainzReleaseGroups.flatMap { releaseGroup ->
                    releaseGroup.artistCredits?.mapIndexed { index, artistCredit ->
                        RoomReleaseGroupArtistCredit(
                            releaseGroupId = releaseGroup.id,
                            artistId = artistCredit.artist.id,
                            name = artistCredit.name,
                            joinPhrase = artistCredit.joinPhrase,
                            order = index
                        )
                    }.orEmpty()
                }
            )

            musicBrainzReleaseGroups
        }
    }

}
