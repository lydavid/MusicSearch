package ly.david.musicsearch.domain.collection.usecase

import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.collection.CollectionRepository
import org.koin.core.annotation.Single

@Single
class GetAllCollections(
    private val collectionRepository: CollectionRepository,
) {
    operator fun invoke(
        showLocal: Boolean = true,
        showRemote: Boolean = true,
        query: String = "",
        entity: MusicBrainzEntity? = null,
    ) = collectionRepository.observeAllCollections(
        showLocal = showLocal,
        showRemote = showRemote,
        query = query,
        entity = entity,
    )
}
