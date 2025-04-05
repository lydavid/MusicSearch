package ly.david.musicsearch.shared.domain.event.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.event.EventsByEntityRepository
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel

class GetEvents(
    private val eventsByEntityRepository: EventsByEntityRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntitiesByEntity<EventListItemModel> {
    override operator fun invoke(
        browseMethod: BrowseMethod?,
        listFilters: ListFilters,
    ): Flow<PagingData<EventListItemModel>> {
        return if (browseMethod == null) {
            emptyFlow()
        } else {
            eventsByEntityRepository.observeEventsByEntity(
                browseMethod = browseMethod,
                listFilters = listFilters,
            )
                .distinctUntilChanged()
                .cachedIn(scope = coroutineScope)
        }
    }
}
