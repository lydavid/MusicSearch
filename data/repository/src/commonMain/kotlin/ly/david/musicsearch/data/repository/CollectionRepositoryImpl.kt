package ly.david.musicsearch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.domain.collection.CollectionRepository

class CollectionRepositoryImpl(
    private val collectionDao: CollectionDao,
) : CollectionRepository {
    override fun observeAllCollections(
        showLocal: Boolean,
        showRemote: Boolean,
        query: String,
        entity: MusicBrainzEntity?,
    ): Flow<PagingData<CollectionListItemModel>> {
        return Pager(
            config = CommonPagingConfig.pagingConfig,
            pagingSourceFactory = {
                collectionDao.getAllCollections(
                    showLocal = showLocal,
                    showRemote = showRemote,
                    query = "%$query%",
                    entity = entity,
                )
            },
        ).flow
    }
}
