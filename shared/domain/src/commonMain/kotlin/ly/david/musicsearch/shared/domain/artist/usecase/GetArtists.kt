package ly.david.musicsearch.shared.domain.artist.usecase

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.artist.ArtistsByEntityRepository
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel

class GetArtists(
    private val artistsByEntityRepository: ArtistsByEntityRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntitiesByEntity<ArtistListItemModel> {
    override operator fun invoke(
        browseMethod: BrowseMethod?,
        listFilters: ListFilters,
    ): Flow<PagingData<ArtistListItemModel>> {
        return if (browseMethod == null) {
            emptyFlow()
        } else {
            artistsByEntityRepository.observeArtistsByEntity(
                browseMethod = browseMethod,
                listFilters = listFilters,
            )
                .distinctUntilChanged()
                .cachedIn(scope = coroutineScope)
        }
    }
}
