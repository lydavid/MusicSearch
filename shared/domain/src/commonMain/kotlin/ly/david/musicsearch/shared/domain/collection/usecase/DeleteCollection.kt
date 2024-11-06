package ly.david.musicsearch.shared.domain.collection.usecase

import ly.david.musicsearch.shared.domain.collection.CollectionRepository

class DeleteCollection(
    private val collectionRepository: CollectionRepository,
) {
    suspend operator fun invoke(
        collectionId: String,
        collectionName: String,
    ) = collectionRepository.deleteCollection(
        collectionId = collectionId,
        collectionName = collectionName,
    )
}
