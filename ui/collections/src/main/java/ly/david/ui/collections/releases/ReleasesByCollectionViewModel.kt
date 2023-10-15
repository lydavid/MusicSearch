package ly.david.ui.collections.releases

import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.release.usecase.GetReleasesByEntity
import ly.david.ui.common.release.ReleasesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleasesByCollectionViewModel(
    getReleasesByEntity: GetReleasesByEntity,
) : ReleasesByEntityViewModel(
    entity = MusicBrainzEntity.COLLECTION,
    getReleasesByEntity = getReleasesByEntity,
)
