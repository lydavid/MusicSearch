package ly.david.musicsearch.shared.domain.relation.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.datetime.Clock
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.RelationRepository

interface GetEntityRelationships {
    operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity?,
        relatedEntities: Set<MusicBrainzEntity>,
        query: String,
    ): Flow<PagingData<ListItemModel>>
}

class GetEntityRelationshipsImpl(
    private val relationRepository: RelationRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntityRelationships {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity?,
        relatedEntities: Set<MusicBrainzEntity>,
        query: String,
    ): Flow<PagingData<ListItemModel>> {
        return when {
            entityId.isEmpty() || entity == null -> emptyFlow()
            else -> relationRepository.observeEntityRelationships(
                entity = entity,
                entityId = entityId,
                relatedEntities = relatedEntities,
                query = query,
                now = Clock.System.now(),
            )
                .cachedIn(scope = coroutineScope)
                .distinctUntilChanged()
        }
    }
}
