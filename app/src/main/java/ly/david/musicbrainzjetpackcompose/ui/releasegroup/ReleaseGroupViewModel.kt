package ly.david.musicbrainzjetpackcompose.ui.releasegroup

import androidx.lifecycle.ViewModel
import ly.david.musicbrainzjetpackcompose.data.MusicBrainzApiService
import ly.david.musicbrainzjetpackcompose.data.ReleaseGroup

class ReleaseGroupViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroup =
        musicBrainzApiService.lookupReleaseGroup(releaseGroupId)
}
