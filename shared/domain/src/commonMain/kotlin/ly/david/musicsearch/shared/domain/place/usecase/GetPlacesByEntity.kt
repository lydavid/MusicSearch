package ly.david.musicsearch.shared.domain.place.usecase

import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.place.PlacesByEntityRepository

class GetPlacesByEntity(
    private val placesByEntityRepository: PlacesByEntityRepository,
) : GetEntitiesByEntity<PlaceListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ) = placesByEntityRepository.observePlacesByEntity(
        entityId = entityId,
        entity = entity,
        listFilters = listFilters,
    )
}
