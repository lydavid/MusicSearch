package ly.david.musicsearch.shared.domain.releasegroup.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsByEntityRepository

class GetReleaseGroups(
    private val releaseGroupsByEntityRepository: ReleaseGroupsByEntityRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntitiesByEntity<ListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): Flow<PagingData<ListItemModel>> {
        return when {
            entityId.isEmpty() || entity == null -> emptyFlow()
            else -> releaseGroupsByEntityRepository.observeReleaseGroupsByEntity(
                entityId = entityId,
                entity = entity,
                listFilters = listFilters,
            )
                .distinctUntilChanged()
                .cachedIn(scope = coroutineScope)
        }
    }
}
