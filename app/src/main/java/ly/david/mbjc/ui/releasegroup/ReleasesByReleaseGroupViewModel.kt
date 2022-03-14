package ly.david.mbjc.ui.releasegroup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import ly.david.mbjc.data.Release
import ly.david.mbjc.data.network.BROWSE_LIMIT
import ly.david.mbjc.data.network.DELAY_PAGED_API_CALLS_MS
import ly.david.mbjc.data.network.MusicBrainzApiService

// TODO: mediator, can we generalize?
@HiltViewModel
class ReleasesByReleaseGroupViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService
): ViewModel() {

    private var initialized = false

    private val allReleases = mutableListOf<Release>()

    // TODO: page, find release group with more than 100 releases
    suspend fun getReleasesByReleaseGroup(
        releaseGroupId: String,
        limit: Int = BROWSE_LIMIT,
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
