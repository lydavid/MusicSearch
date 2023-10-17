package ly.david.musicsearch.domain.collection.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.collection.CollectionRepository
import org.koin.core.annotation.Single

@Single
class GetAllCollections(
    private val collectionRepository: CollectionRepository,
) {
    operator fun invoke(
        username: String = "",
        showLocal: Boolean = true,
        showRemote: Boolean = true,
        query: String = "",
        entity: MusicBrainzEntity? = null,
    ): Flow<PagingData<CollectionListItemModel>> {
        return collectionRepository.observeAllCollections(
            username = username,
            showLocal = showLocal,
            showRemote = showRemote,
            query = query,
            entity = entity,
        )
    }
}
