package ly.david.musicsearch.shared.domain.collection.usecase

import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.error.ActionableResult

class DeleteCollection(
    private val collectionRepository: CollectionRepository,
) {
    suspend operator fun invoke(
        collectionIds: Set<String>,
    ): ActionableResult {
        return collectionRepository.deleteCollections(
            collectionIds = collectionIds,
        )
    }
}
