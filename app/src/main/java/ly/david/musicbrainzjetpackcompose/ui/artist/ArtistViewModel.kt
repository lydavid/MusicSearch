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

    private var initialized = false

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
            delay(DELAY_RECURSIVE_API_CALLS_MS)
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
