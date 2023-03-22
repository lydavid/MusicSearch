package ly.david.mbjc.ui.common.screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.coverart.GetReleaseGroupCoverArtPath
import ly.david.data.coverart.UpdateReleaseGroupCoverArtDao
import ly.david.data.coverart.api.CoverArtArchiveApiService
import ly.david.data.persistence.releasegroup.ReleaseGroupDao

@HiltViewModel
internal class ReleaseGroupsListViewModel @Inject constructor(
    override val coverArtArchiveApiService: CoverArtArchiveApiService,
    private val releaseGroupDao: ReleaseGroupDao
) : ViewModel(), GetReleaseGroupCoverArtPath {
    override val updateReleaseGroupCoverArtDao: UpdateReleaseGroupCoverArtDao
        get() = releaseGroupDao
}
