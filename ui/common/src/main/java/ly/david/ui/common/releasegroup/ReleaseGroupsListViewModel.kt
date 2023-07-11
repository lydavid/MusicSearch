package ly.david.ui.common.releasegroup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.coverart.ReleaseGroupImageManager
import ly.david.data.coverart.api.CoverArtArchiveApiService
import ly.david.data.image.ImageUrlSaver

@HiltViewModel
class ReleaseGroupsListViewModel @Inject constructor(
    override val coverArtArchiveApiService: CoverArtArchiveApiService,
    override val imageUrlSaver: ImageUrlSaver,
) : ViewModel(), ReleaseGroupImageManager
