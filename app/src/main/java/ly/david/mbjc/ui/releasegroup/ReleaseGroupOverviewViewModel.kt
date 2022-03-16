package ly.david.mbjc.ui.releasegroup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.domain.UiReleaseGroup

// TODO: move this
@HiltViewModel
class ReleaseGroupOverviewViewModel @Inject constructor(
    private val releaseGroupRepository: ReleaseGroupRepository
) : ViewModel() {

    suspend fun lookupReleaseGroup(releaseGroupId: String): UiReleaseGroup =
        releaseGroupRepository.lookupReleaseGroup(releaseGroupId)
}
