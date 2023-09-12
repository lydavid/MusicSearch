package ly.david.ui.common.releasegroup

import androidx.lifecycle.ViewModel
import ly.david.data.coverart.ReleaseGroupImageRepository
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ReleaseGroupsListViewModel(
    private val releaseGroupImageRepository: ReleaseGroupImageRepository,
) : ViewModel() {
    suspend fun getReleaseGroupCoverArtUrlFromNetwork(
        releaseGroupId: String,
    ) = releaseGroupImageRepository.getReleaseGroupCoverArtUrlFromNetwork(
        releaseGroupId = releaseGroupId,
        thumbnail = true,
    )
}
