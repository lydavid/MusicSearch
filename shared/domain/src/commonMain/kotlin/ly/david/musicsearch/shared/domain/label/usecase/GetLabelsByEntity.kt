package ly.david.musicsearch.shared.domain.label.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.label.LabelsByEntityRepository
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class GetLabelsByEntity(
    private val labelsByEntityRepository: LabelsByEntityRepository,
) : GetEntitiesByEntity<LabelListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): Flow<PagingData<LabelListItemModel>> {
        return when {
            entityId.isEmpty() || entity == null -> emptyFlow()
            else -> labelsByEntityRepository.observeLabelsByEntity(
                entityId = entityId,
                entity = entity,
                listFilters = listFilters,
            )
        }
    }
}
