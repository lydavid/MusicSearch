package ly.david.musicsearch.domain.label.usecase

import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.LabelListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.domain.label.LabelsByEntityRepository
import org.koin.core.annotation.Single

@Single
class GetLabelsByEntity(
    private val labelsByEntityRepository: LabelsByEntityRepository,
) : GetEntitiesByEntity<LabelListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ) = labelsByEntityRepository.observeLabelsByEntity(
        entityId = entityId,
        entity = entity,
        listFilters = listFilters,
    )
}
