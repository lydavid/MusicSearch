package ly.david.musicsearch.shared.domain.collection.usecase

import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel

class GetCollection(
    private val collectionRepository: CollectionRepository,
    private val visitedDao: VisitedDao,
) {
    operator fun invoke(
        entityId: String,
    ): CollectionListItemModel? {
        visitedDao.insert(entityId)
        return collectionRepository.getCollection(entityId)
    }
}
