package ly.david.musicsearch.shared.domain.collection.usecase

import ly.david.musicsearch.shared.domain.collection.CollectionRepository

class GetCollection(
    private val collectionRepository: CollectionRepository,
) {
    operator fun invoke(
        entityId: String,
    ) = collectionRepository.getCollection(entityId)
}
