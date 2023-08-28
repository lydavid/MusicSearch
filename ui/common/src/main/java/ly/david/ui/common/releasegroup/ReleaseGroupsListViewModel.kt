package ly.david.ui.common.releasegroup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.coverart.ReleaseGroupImageRepository

@HiltViewModel
class ReleaseGroupsListViewModel @Inject constructor(
    private val releaseGroupImageRepository: ReleaseGroupImageRepository,
) : ViewModel() {
    suspend fun getReleaseGroupCoverArtUrlFromNetwork(
        releaseGroupId: String,
    ) = releaseGroupImageRepository.getReleaseGroupCoverArtUrlFromNetwork(
        releaseGroupId = releaseGroupId,
        thumbnail = true,
    )
}
