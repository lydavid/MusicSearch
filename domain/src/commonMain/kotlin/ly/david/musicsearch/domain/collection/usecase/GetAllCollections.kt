package ly.david.musicsearch.domain.collection.usecase

import app.cash.paging.PagingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import ly.david.musicsearch.core.models.auth.MusicBrainzAuthStore
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.collection.CollectionRepository
import org.koin.core.annotation.Single

@OptIn(ExperimentalCoroutinesApi::class)
@Single
class GetAllCollections(
    private val collectionRepository: CollectionRepository,
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
) {
    operator fun invoke(
        showLocal: Boolean = true,
        showRemote: Boolean = true,
        query: String = "",
        entity: MusicBrainzEntity? = null,
    ): Flow<PagingData<CollectionListItemModel>> {
        return musicBrainzAuthStore.username.flatMapLatest { username ->
            collectionRepository.observeAllCollections(
                username = username,
                showLocal = showLocal,
                showRemote = showRemote,
                query = query,
                entity = entity,
            )
        }
    }
}
