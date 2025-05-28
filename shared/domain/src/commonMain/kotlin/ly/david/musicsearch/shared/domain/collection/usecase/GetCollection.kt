package ly.david.musicsearch.shared.domain.collection.usecase

import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel

class GetCollection(
    private val collectionRepository: CollectionRepository,
    private val detailsMetadataDao: DetailsMetadataDao,
) {
    operator fun invoke(
        entityId: String,
    ): CollectionListItemModel? {
        detailsMetadataDao.upsert(entityId = entityId)
        return collectionRepository.getCollection(entityId)
    }
}
