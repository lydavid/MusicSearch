package ly.david.musicsearch.shared.domain.series.usecase

import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.series.SeriesByEntityRepository

class GetSeriesByEntity(
    private val seriesByEntityRepository: SeriesByEntityRepository,
) : GetEntitiesByEntity<SeriesListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ) = seriesByEntityRepository.observeSeriesByEntity(
        entityId = entityId,
        entity = entity,
        listFilters = listFilters,
    )
}
