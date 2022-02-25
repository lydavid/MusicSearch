package ly.david.musicbrainzjetpackcompose.ui.release

import androidx.lifecycle.ViewModel
import ly.david.musicbrainzjetpackcompose.data.MusicBrainzApiService
import ly.david.musicbrainzjetpackcompose.data.Release

class ReleaseViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    private var release: Release? = null

    suspend fun lookupRelease(releaseId: String, ): Release =
        release ?: musicBrainzApiService.lookupRelease(releaseId).also { release = it }
}
