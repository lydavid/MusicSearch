package ly.david.ui.commonlegacy.releasegroup

import androidx.lifecycle.ViewModel
import ly.david.musicsearch.domain.releasegroup.ReleaseGroupImageRepository
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
