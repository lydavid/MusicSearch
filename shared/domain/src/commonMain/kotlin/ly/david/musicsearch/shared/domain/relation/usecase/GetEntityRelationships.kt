package ly.david.musicsearch.shared.domain.relation.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlin.time.Clock
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationRepository

interface GetEntityRelationships {
    operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntityType?,
        relatedEntities: Set<MusicBrainzEntityType>,
        query: String,
    ): Flow<PagingData<ListItemModel>>
}

class GetEntityRelationshipsImpl(
    private val relationRepository: RelationRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntityRelationships {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntityType?,
        relatedEntities: Set<MusicBrainzEntityType>,
        query: String,
    ): Flow<PagingData<ListItemModel>> {
        return when {
            entityId.isEmpty() || entity == null -> emptyFlow()
            else -> relationRepository.observeEntityRelationships(
                entity = entity,
                entityId = entityId,
                relatedEntities = relatedEntities,
                query = query,
                lastUpdated = Clock.System.now(),
            )
                .cachedIn(scope = coroutineScope)
                .distinctUntilChanged()
        }
    }
}
