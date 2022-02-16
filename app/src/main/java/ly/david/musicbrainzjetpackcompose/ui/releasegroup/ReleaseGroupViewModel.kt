package ly.david.musicbrainzjetpackcompose.ui.releasegroup

import androidx.lifecycle.ViewModel
import ly.david.musicbrainzjetpackcompose.musicbrainz.MusicBrainzApiService
import ly.david.musicbrainzjetpackcompose.musicbrainz.ReleaseGroup

class ReleaseGroupViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroup =
        musicBrainzApiService.lookupReleaseGroup(releaseGroupId)
}
