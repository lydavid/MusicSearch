package ly.david.ui.collections.places

import ly.david.musicsearch.core.models.listitem.PlaceListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.place.usecase.GetPlacesByEntity
import ly.david.ui.common.EntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class PlacesByCollectionViewModel(
    getPlacesByEntity: GetPlacesByEntity,
) : EntitiesByEntityViewModel<PlaceListItemModel>(
    entity = MusicBrainzEntity.COLLECTION,
    getEntitiesByEntity = getPlacesByEntity,
)
