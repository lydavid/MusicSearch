package ly.david.ui.common.release

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.coverart.ReleaseImageRepository

@HiltViewModel
class ReleasesListViewModel @Inject constructor(
    private val releaseImageRepository: ReleaseImageRepository,
) : ViewModel() {
    suspend fun getReleaseCoverArtUrlFromNetwork(
        releaseId: String,
    ) = releaseImageRepository.getReleaseCoverArtUrlFromNetwork(
        releaseId = releaseId,
        thumbnail = true,
    )
}
