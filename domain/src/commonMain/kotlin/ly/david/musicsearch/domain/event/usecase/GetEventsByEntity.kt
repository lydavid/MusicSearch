package ly.david.musicsearch.domain.event.usecase

import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.EventListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.base.GetEntitiesByEntity
import ly.david.musicsearch.domain.event.EventsByEntityRepository
import org.koin.core.annotation.Single

@Single
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
