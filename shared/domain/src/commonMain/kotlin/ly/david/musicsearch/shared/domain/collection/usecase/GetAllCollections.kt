package ly.david.musicsearch.shared.domain.collection.usecase

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.collection.CollectionRepository

@OptIn(ExperimentalCoroutinesApi::class)
class GetAllCollections(
    private val collectionRepository: CollectionRepository,
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val coroutineScope: CoroutineScope,
) {
    operator fun invoke(
        entity: MusicBrainzEntity? = null,
        query: String = "",
        showLocal: Boolean = true,
        showRemote: Boolean = true,
        sortOption: CollectionSortOption = CollectionSortOption.ALPHABETICALLY,
    ): Flow<PagingData<CollectionListItemModel>> {
        return musicBrainzAuthStore.username.flatMapLatest { username ->
            collectionRepository.observeAllCollections(
                username = username,
                entity = entity,
                query = query,
                showLocal = showLocal,
                showRemote = showRemote,
                sortOption = sortOption,
            )
        }
            .distinctUntilChanged()
            .cachedIn(coroutineScope)
    }
}
