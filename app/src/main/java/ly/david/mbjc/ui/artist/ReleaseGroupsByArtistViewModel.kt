package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import ly.david.mbjc.data.MusicBrainzApiService
import ly.david.mbjc.data.ReleaseGroup
import ly.david.mbjc.preferences.DELAY_PAGED_API_CALLS_MS
import ly.david.mbjc.preferences.MAX_BROWSE_LIMIT

// TODO: Let's have one viewmodel for each tab, they should stay alive
class ReleaseGroupsByArtistViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    private var initialized = false

    // TODO: use pager3, as we scroll, will it cause crazy jumps as we group?
    // TODO: Would be nice if view can listen to this
    //  and we would emit data every time one api is complete, meaning user doesn't have to wait for all api to complete
    //  to start viewing data
    //  and when new data is emitted, we don't force scroll to the top
    private val allReleaseGroups = mutableListOf<ReleaseGroup>()

    suspend fun getReleaseGroupsByArtist(
        artistId: String,
        limit: Int = MAX_BROWSE_LIMIT,
        offset: Int = 0
    ): List<ReleaseGroup> {
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
            allReleaseGroups
        }
    }


}
