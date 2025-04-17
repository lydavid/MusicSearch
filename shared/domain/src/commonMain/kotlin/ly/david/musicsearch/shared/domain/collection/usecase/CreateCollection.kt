package ly.david.musicsearch.shared.domain.collection.usecase

import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.collection.CreateNewCollectionResult
import ly.david.musicsearch.shared.domain.common.getUUID
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel

class CreateCollection(
    private val collectionRepository: CollectionRepository,
) {
    operator fun invoke(
        newCollection: CreateNewCollectionResult.NewCollection,
    ) = collectionRepository.insertLocal(
        CollectionListItemModel(
            id = newCollection.id ?: getUUID(),
            name = newCollection.name,
            entity = newCollection.entity,
            cachedEntityCount = 0,
            isRemote = false,
        ),
    )
}
