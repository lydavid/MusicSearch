package ly.david.musicbrainzjetpackcompose.ui.releasegroup

import androidx.lifecycle.ViewModel
import ly.david.musicbrainzjetpackcompose.data.MusicBrainzApiService
import ly.david.musicbrainzjetpackcompose.data.ReleaseGroup
import ly.david.musicbrainzjetpackcompose.data.coverart.CoverArtArchiveApiService
import ly.david.musicbrainzjetpackcompose.data.coverart.CoverArtsResponse

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
