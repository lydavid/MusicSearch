package ly.david.musicsearch.domain.artist.usecase

import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.ArtistListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.artist.ArtistsByEntityRepository
import ly.david.musicsearch.domain.base.GetEntitiesByEntity
import org.koin.core.annotation.Single

@Single
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
