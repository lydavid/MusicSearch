package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import ly.david.mbjc.data.MusicBrainzApiService
import ly.david.mbjc.data.MusicBrainzReleaseGroup
import ly.david.mbjc.data.ReleaseGroupArtistDao
import ly.david.mbjc.data.ReleaseGroupDao
import ly.david.mbjc.data.persistence.ReleaseGroupArtist
import ly.david.mbjc.data.toRoomReleaseGroup
import ly.david.mbjc.preferences.DELAY_PAGED_API_CALLS_MS
import ly.david.mbjc.preferences.MAX_BROWSE_LIMIT

@HiltViewModel
class ReleaseGroupsByArtistViewModel @Inject constructor(
    private val releaseGroupDao: ReleaseGroupDao,
    private val releaseGroupArtistDao: ReleaseGroupArtistDao
) : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    private var initialized = false

    // TODO: use pager3, as we scroll, will it cause crazy jumps as we group?
    // TODO: Would be nice if view can listen to this
    //  and we would emit data every time one api is complete, meaning user doesn't have to wait for all api to complete
    //  to start viewing data
    //  and when new data is emitted, we don't force scroll to the top
    private val allReleaseGroups = mutableListOf<MusicBrainzReleaseGroup>()

    suspend fun getReleaseGroupsByArtist(
        artistId: String,
        limit: Int = MAX_BROWSE_LIMIT,
        offset: Int = 0
    ): List<MusicBrainzReleaseGroup> {
        if (initialized) {
            return allReleaseGroups
        }
        if (offset != 0) {
            delay(DELAY_PAGED_API_CALLS_MS)
        }
        val response = musicBrainzApiService.browseReleaseGroupsByArtist(
            artistId = artistId,
            limit = limit,
            offset = offset
        )

        val newReleaseGroups = response.releaseGroups
        allReleaseGroups.addAll(newReleaseGroups)
        return if (allReleaseGroups.size < response.releaseGroupCount) {
            getReleaseGroupsByArtist(
                artistId = artistId,
                offset = offset + newReleaseGroups.size
            )
        } else {
            initialized = true
            releaseGroupDao.insertAll(allReleaseGroups.map { it.toRoomReleaseGroup() })

            releaseGroupArtistDao.insertAll(
                allReleaseGroups.flatMap { releaseGroup ->
                    releaseGroup.artistCredits?.mapIndexed { index, artistCredit ->
                        ReleaseGroupArtist(
                            releaseGroupId = releaseGroup.id,
                            artistId = artistCredit.artist.id,
                            name = artistCredit.name,
                            joinPhrase = artistCredit.joinPhrase,
                            order = index
                        )
                    }.orEmpty()
                }
            )

            allReleaseGroups
        }
    }

}
