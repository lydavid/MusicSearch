package ly.david.musicsearch.shared.domain.relation.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.appendPlaceholderLastUpdatedBanner
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class GetEntityRelationships(
    private val relationRepository: RelationRepository,
    private val coroutineScope: CoroutineScope,
) {
    operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity?,
        relatedEntities: Set<MusicBrainzEntity>,
        query: String,
    ): Flow<PagingData<ListItemModel>> {
        return when {
            entityId.isEmpty() || entity == null -> emptyFlow()
            else -> relationRepository.observeEntityRelationships(
                entityId = entityId,
                entity = entity,
                relatedEntities = relatedEntities,
                query = query,
            )
                .cachedIn(scope = coroutineScope)
                .appendPlaceholderLastUpdatedBanner()
                .distinctUntilChanged()
        }
    }
}
