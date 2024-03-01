package ly.david.musicsearch.shared.feature.collections.releasegroups

import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.releasegroup.usecase.GetReleaseGroupsByEntity
import ly.david.ui.commonlegacy.EntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleaseGroupsByCollectionViewModel(
    getReleaseGroupsByEntity: GetReleaseGroupsByEntity,
) : EntitiesByEntityViewModel<ListItemModel>(
    entity = MusicBrainzEntity.COLLECTION,
    getEntitiesByEntity = getReleaseGroupsByEntity,
)
