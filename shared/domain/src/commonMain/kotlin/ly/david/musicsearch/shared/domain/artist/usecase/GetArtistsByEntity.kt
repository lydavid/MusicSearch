package ly.david.musicsearch.shared.domain.artist.usecase

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
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
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): Flow<PagingData<ArtistListItemModel>> {
        return when {
            entityId.isEmpty() || entity == null -> emptyFlow()
            else -> artistsByEntityRepository.observeArtistsByEntity(
                entityId = entityId,
                entity = entity,
                listFilters = listFilters,
            )
        }
    }
}
