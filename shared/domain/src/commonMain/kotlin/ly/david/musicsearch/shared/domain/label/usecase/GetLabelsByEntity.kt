package ly.david.musicsearch.shared.domain.label.usecase

import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.label.LabelsByEntityRepository

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
