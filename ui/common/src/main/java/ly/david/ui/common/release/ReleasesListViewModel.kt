package ly.david.ui.common.release

import androidx.lifecycle.ViewModel
import ly.david.data.coverart.ReleaseImageRepository
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ReleasesListViewModel(
    private val releaseImageRepository: ReleaseImageRepository,
) : ViewModel() {
    suspend fun getReleaseCoverArtUrlFromNetwork(
        releaseId: String,
    ) = releaseImageRepository.getReleaseCoverArtUrlFromNetwork(
        releaseId = releaseId,
        thumbnail = true,
    )
}
