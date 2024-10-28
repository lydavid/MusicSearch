package ly.david.musicsearch.shared.domain.relation.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class GetEntityRelationships(
    private val relationRepository: RelationRepository,
    private val coroutineScope: CoroutineScope,
) {
    operator fun invoke(
        entity: MusicBrainzEntity,
        entityId: String,
        query: String,
    ): Flow<PagingData<RelationListItemModel>> = relationRepository.observeEntityRelationshipsExcludingUrls(
        entity = entity,
        entityId = entityId,
        query = query,
    )
        .distinctUntilChanged()
        .cachedIn(scope = coroutineScope)
}
