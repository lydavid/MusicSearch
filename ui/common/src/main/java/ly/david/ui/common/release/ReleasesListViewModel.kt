package ly.david.ui.common.release

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.coverart.ReleaseImageManager
import ly.david.data.image.ImageUrlSaver
import ly.david.data.coverart.api.CoverArtArchiveApiService

@HiltViewModel
class ReleasesListViewModel @Inject constructor(
    override val coverArtArchiveApiService: CoverArtArchiveApiService,
    override val imageUrlSaver: ImageUrlSaver,
) : ViewModel(), ReleaseImageManager
