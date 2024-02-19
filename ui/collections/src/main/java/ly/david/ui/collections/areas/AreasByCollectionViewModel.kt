package ly.david.ui.collections.areas

import ly.david.musicsearch.core.models.listitem.AreaListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.area.usecase.GetAreasByEntity
import ly.david.ui.commonlegacy.EntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class AreasByCollectionViewModel(
    getAreasByEntity: GetAreasByEntity,
) : EntitiesByEntityViewModel<AreaListItemModel>(
    entity = MusicBrainzEntity.COLLECTION,
    getEntitiesByEntity = getAreasByEntity,
)
