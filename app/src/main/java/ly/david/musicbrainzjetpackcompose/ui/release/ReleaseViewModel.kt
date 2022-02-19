package ly.david.musicbrainzjetpackcompose.ui.release

import androidx.lifecycle.ViewModel
import ly.david.musicbrainzjetpackcompose.data.MusicBrainzApiService
import ly.david.musicbrainzjetpackcompose.data.Release

class ReleaseViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    suspend fun lookupRelease(
        releaseId: String,
    ): Release {
        return musicBrainzApiService.lookupRelease(releaseId)
    }
}
