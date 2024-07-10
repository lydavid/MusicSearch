package ly.david.musicsearch.shared.domain.event.usecase

import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.EventListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.event.EventsByEntityRepository

class GetEventsByEntity(
    private val eventsByEntityRepository: EventsByEntityRepository,
) : GetEntitiesByEntity<EventListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ) = eventsByEntityRepository.observeEventsByEntity(
        entityId = entityId,
        entity = entity,
        listFilters = listFilters,
    )
}
