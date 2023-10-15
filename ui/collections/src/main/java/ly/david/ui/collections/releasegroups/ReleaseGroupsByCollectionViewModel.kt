package ly.david.ui.collections.releasegroups

import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.releasegroup.usecase.GetReleaseGroupsByEntity
import ly.david.ui.common.releasegroup.ReleaseGroupsByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleaseGroupsByCollectionViewModel(
    getReleaseGroupsByEntity: GetReleaseGroupsByEntity,
) : ReleaseGroupsByEntityViewModel(
    entity = MusicBrainzEntity.COLLECTION,
    getReleaseGroupsByEntity = getReleaseGroupsByEntity,
)
