package ly.david.musicsearch.shared.domain.collection.usecase

import ly.david.musicsearch.shared.domain.collection.CollectionRepository

class DeleteFromCollection(
    private val collectionRepository: CollectionRepository,
) {
    suspend operator fun invoke(
        collectionId: String,
        entityId: String,
        entityName: String,
    ) = collectionRepository.deleteFromCollection(
        collectionId = collectionId,
        entityId = entityId,
        entityName = entityName,
    )
}
