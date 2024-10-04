package ly.david.musicsearch.shared.domain.genre.usecase

import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.GenreListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.genre.GenresByEntityRepository

class GetGenresByEntity(
    private val genresByEntityRepository: GenresByEntityRepository,
) : GetEntitiesByEntity<GenreListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ) = genresByEntityRepository.observeGenresByEntity(
        entityId = entityId,
        entity = entity,
        listFilters = listFilters,
    )
}
