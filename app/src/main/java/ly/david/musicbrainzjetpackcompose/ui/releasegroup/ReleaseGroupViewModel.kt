package ly.david.musicbrainzjetpackcompose.ui.releasegroup

import androidx.lifecycle.ViewModel
import ly.david.musicbrainzjetpackcompose.coverartarchive.CoverArtArchiveApiService
import ly.david.musicbrainzjetpackcompose.coverartarchive.CoverArtsResponse
import ly.david.musicbrainzjetpackcompose.musicbrainz.MusicBrainzApiService
import ly.david.musicbrainzjetpackcompose.musicbrainz.ReleaseGroup

class ReleaseGroupViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    private val coverArtArchiveApiService by lazy {
        CoverArtArchiveApiService.create()
    }

    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroup =
        musicBrainzApiService.lookupReleaseGroup(releaseGroupId)

    suspend fun getCoverArts(releaseGroupId: String): CoverArtsResponse =
        coverArtArchiveApiService.getCoverArts(releaseGroupId)
}
