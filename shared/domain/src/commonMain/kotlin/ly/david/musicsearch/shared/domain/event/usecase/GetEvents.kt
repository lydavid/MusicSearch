package ly.david.musicsearch.shared.domain.event.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.event.EventsByEntityRepository
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class GetEvents(
    private val eventsByEntityRepository: EventsByEntityRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntitiesByEntity<EventListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): Flow<PagingData<EventListItemModel>> {
        return eventsByEntityRepository.observeEventsByEntity(
            entityId = entityId,
            entity = entity,
            listFilters = listFilters,
        )
            .distinctUntilChanged()
            .cachedIn(scope = coroutineScope)
    }
}
