package ly.david.musicsearch.domain.collection.usecase

import ly.david.musicsearch.domain.collection.CollectionRepository
import org.koin.core.annotation.Single

@Single
class GetCollection(
    private val collectionRepository: CollectionRepository,
) {
    operator fun invoke(
        entityId: String,
    ) = collectionRepository.getCollection(entityId)
}
