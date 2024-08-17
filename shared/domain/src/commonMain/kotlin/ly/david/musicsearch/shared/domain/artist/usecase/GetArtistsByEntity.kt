package ly.david.musicsearch.shared.domain.artist.usecase

import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.artist.ArtistsByEntityRepository
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity

class GetArtistsByEntity(
    private val artistsByEntityRepository: ArtistsByEntityRepository,
) : GetEntitiesByEntity<ArtistListItemModel> {
    override operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ) = artistsByEntityRepository.observeArtistsByEntity(
        entityId = entityId,
        entity = entity,
        listFilters = listFilters,
    )
}
