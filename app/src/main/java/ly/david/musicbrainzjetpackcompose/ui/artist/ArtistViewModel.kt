package ly.david.musicbrainzjetpackcompose.ui.artist

import androidx.lifecycle.ViewModel
import ly.david.musicbrainzjetpackcompose.data.MusicBrainzApiService
import ly.david.musicbrainzjetpackcompose.data.ReleaseGroup
import ly.david.musicbrainzjetpackcompose.data.browse.DEFAULT_BROWSE_LIMIT

class ArtistViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    private val allReleaseGroups = mutableListOf<ReleaseGroup>()

    suspend fun getReleaseGroupsByArtist(
        artistId: String,
        limit: Int = DEFAULT_BROWSE_LIMIT,
        offset: Int = 0
    ): List<ReleaseGroup> {
        val response = musicBrainzApiService.browseReleaseGroupsByArtist(
            artistId = artistId,
            limit = limit,
            offset = offset
        )

        if (offset == 0) allReleaseGroups.clear()

        val newReleaseGroups = response.releaseGroups
        allReleaseGroups.addAll(newReleaseGroups)
        return if (newReleaseGroups.size >= limit) {
            getReleaseGroupsByArtist(artistId = artistId, offset = offset + newReleaseGroups.size)
        } else {
            allReleaseGroups
        }

        // TODO: edge case of exactly 100 release groups
        //  will prob have to make one more call only to find nothing else
    }
}
