package ly.david.ui.common.release

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.coverart.GetReleaseCoverArtPath
import ly.david.data.coverart.UpdateReleaseCoverArtDao
import ly.david.data.coverart.api.CoverArtArchiveApiService
import ly.david.data.persistence.release.ReleaseDao

@HiltViewModel
class ReleasesListViewModel @Inject constructor(
    override val coverArtArchiveApiService: CoverArtArchiveApiService,
    private val releaseDao: ReleaseDao
) : ViewModel(), GetReleaseCoverArtPath {
    override val updateReleaseCoverArtDao: UpdateReleaseCoverArtDao
        get() = releaseDao
}
