package ly.david.musicbrainzjetpackcompose.ui.releasegroup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import ly.david.musicbrainzjetpackcompose.data.MusicBrainzApiService
import ly.david.musicbrainzjetpackcompose.data.Release
import ly.david.musicbrainzjetpackcompose.preferences.DELAY_RECURSIVE_API_CALLS_MS
import ly.david.musicbrainzjetpackcompose.preferences.MAX_BROWSE_LIMIT

class ReleaseGroupViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    suspend fun getReleasesByReleaseGroup(
        releaseGroupId: String,
        limit: Int = MAX_BROWSE_LIMIT,
        offset: Int = 0,
        currentReleases: List<Release> = listOf()
    ): List<Release> {
        if (offset != 0) {
            delay(DELAY_RECURSIVE_API_CALLS_MS)
        }
        val response = musicBrainzApiService.browseReleasesByReleaseGroup(
            releaseGroupId = releaseGroupId,
            limit = limit,
            offset = offset
        )

        val newReleases = response.releases
        val mergedReleases = currentReleases + newReleases
        return if (mergedReleases.size < response.releaseCount) {
            getReleasesByReleaseGroup(
                releaseGroupId = releaseGroupId,
                offset = offset + newReleases.size,
                currentReleases = mergedReleases
            )
        } else {
            mergedReleases
        }
    }
}
