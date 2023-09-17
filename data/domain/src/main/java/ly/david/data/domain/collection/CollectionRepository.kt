package ly.david.data.domain.collection

import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import lydavidmusicsearchdatadatabase.Collection
import lydavidmusicsearchdatadatabase.Collection_entity
import org.koin.core.annotation.Single

data class CollectionWithEntities(
    val collection: Collection,
    val entities: List<Collection_entity>,
)

@Single
class CollectionRepository(
    private val collectionDao: CollectionDao,
    private val collectionEntityDao: CollectionEntityDao,
) {
//    fun getAllCollections(
//        showLocal: Boolean = true,
//        showRemote: Boolean = true,
//        query: String,
//    ): PagingSource<Int, CollectionWithEntities> {
//        return collectionDao.getAllCollections(
//            showLocal, showRemote, query
//        )
//    }
}
