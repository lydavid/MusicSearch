package ly.david.ui.commonlegacy.release

import androidx.lifecycle.ViewModel
import ly.david.musicsearch.domain.release.ReleaseImageRepository
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
