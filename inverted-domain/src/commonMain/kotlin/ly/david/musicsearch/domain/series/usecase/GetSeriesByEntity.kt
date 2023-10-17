package ly.david.musicsearch.domain.series.usecase

import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.SeriesListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.base.GetEntitiesByEntity
import ly.david.musicsearch.domain.series.SeriesByEntityRepository
import org.koin.core.annotation.Single

@Single
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
