package ly.david.mbjc.ui.release

import androidx.lifecycle.ViewModel
import ly.david.mbjc.data.MusicBrainzApiService
import ly.david.mbjc.data.Release

class ReleaseViewModel : ViewModel() {

    private val musicBrainzApiService by lazy {
        MusicBrainzApiService.create()
    }

    private var release: Release? = null

    suspend fun lookupRelease(releaseId: String, ): Release =
        release ?: musicBrainzApiService.lookupRelease(releaseId).also { release = it }
}
