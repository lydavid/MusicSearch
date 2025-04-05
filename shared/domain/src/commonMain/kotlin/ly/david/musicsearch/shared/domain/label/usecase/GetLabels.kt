package ly.david.musicsearch.shared.domain.label.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.label.LabelsByEntityRepository
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class GetLabels(
    private val labelsByEntityRepository: LabelsByEntityRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntitiesByEntity<LabelListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): Flow<PagingData<LabelListItemModel>> {
        return labelsByEntityRepository.observeLabelsByEntity(
            entityId = entityId,
            entity = entity,
            listFilters = listFilters,
        )
            .distinctUntilChanged()
            .cachedIn(scope = coroutineScope)
    }
}
