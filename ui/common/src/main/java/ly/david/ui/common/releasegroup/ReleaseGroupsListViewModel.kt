package ly.david.ui.common.releasegroup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.coverart.ReleaseGroupImageManager
import ly.david.data.image.ImageUrlSaver
import ly.david.data.coverart.api.CoverArtArchiveApiService

@HiltViewModel
class ReleaseGroupsListViewModel @Inject constructor(
    override val coverArtArchiveApiService: CoverArtArchiveApiService,
    override val imageUrlSaver: ImageUrlSaver
) : ViewModel(), ReleaseGroupImageManager
