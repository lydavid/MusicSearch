package ly.david.musicsearch.shared.domain.collection.usecase

import app.cash.paging.PagingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import ly.david.musicsearch.core.models.auth.MusicBrainzAuthStore
import ly.david.musicsearch.core.models.collection.CollectionSortOption
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import org.koin.core.annotation.Single

@OptIn(ExperimentalCoroutinesApi::class)
@Single
class GetAllCollections(
    private val collectionRepository: CollectionRepository,
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
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
    }
}
