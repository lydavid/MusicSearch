package ly.david.musicsearch.domain.collection.usecase

import ly.david.musicsearch.domain.collection.CollectionRepository
import org.koin.core.annotation.Single

@Single
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
