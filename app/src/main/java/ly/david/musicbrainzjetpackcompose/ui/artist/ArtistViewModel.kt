package ly.david.musicbrainzjetpackcompose.ui.artist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import ly.david.musicbrainzjetpackcompose.data.MusicBrainzApiService
import ly.david.musicbrainzjetpackcompose.data.ReleaseGroup
import ly.david.musicbrainzjetpackcompose.preferences.DELAY_RECURSIVE_API_CALLS_MS
import ly.david.musicbrainzjetpackcompose.preferences.MAX_BROWSE_LIMIT

// TODO: will we have this one viewmodel for all the tabs in Artist screen?
//  or should each tab have its own?
class ArtistViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

     suspend fun getReleaseGroupsByArtist(
        artistId: String,
        limit: Int = MAX_BROWSE_LIMIT,
        offset: Int = 0,
        currentReleaseGroups: List<ReleaseGroup> = listOf()
    ): List<ReleaseGroup> {
        if (offset != 0) {
            delay(DELAY_RECURSIVE_API_CALLS_MS)
        }
        val response = musicBrainzApiService.browseReleaseGroupsByArtist(
            artistId = artistId,
            limit = limit,
            offset = offset
        )

        val newReleaseGroups = response.releaseGroups
        val mergedReleaseGroups = currentReleaseGroups + newReleaseGroups
        return if (mergedReleaseGroups.size < response.releaseGroupCount) {
            getReleaseGroupsByArtist(
                artistId = artistId,
                offset = offset + newReleaseGroups.size,
                currentReleaseGroups = mergedReleaseGroups
            )
        } else {
            mergedReleaseGroups
        }
    }
}
