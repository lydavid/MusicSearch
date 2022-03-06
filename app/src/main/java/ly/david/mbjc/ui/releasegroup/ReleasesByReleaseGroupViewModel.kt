package ly.david.mbjc.ui.releasegroup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import ly.david.mbjc.data.Release
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.preferences.DELAY_PAGED_API_CALLS_MS
import ly.david.mbjc.preferences.MAX_BROWSE_LIMIT

class ReleasesByReleaseGroupViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    private var initialized = false

    private val allReleases = mutableListOf<Release>()

    suspend fun getReleasesByReleaseGroup(
        releaseGroupId: String,
        limit: Int = MAX_BROWSE_LIMIT,
        offset: Int = 0
    ): List<Release> {
        if (initialized) {
            return allReleases
        }
        if (offset != 0) {
            delay(DELAY_PAGED_API_CALLS_MS)
        }
        val response = musicBrainzApiService.browseReleasesByReleaseGroup(
            releaseGroupId = releaseGroupId,
            limit = limit,
            offset = offset
        )

        val newReleases = response.releases
        allReleases.addAll(newReleases)
        return if (allReleases.size < response.releaseCount) {
            getReleasesByReleaseGroup(
                releaseGroupId = releaseGroupId,
                offset = offset + newReleases.size
            )
        } else {
            initialized = true
            allReleases
        }
    }
}
