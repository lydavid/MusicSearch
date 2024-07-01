package ly.david.musicsearch.domain.collection

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.ActionableResult
import ly.david.musicsearch.core.models.collection.CollectionSortOption
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

interface CollectionRepository {
    fun observeAllCollections(
        username: String,
        entity: MusicBrainzEntity?,
        query: String,
        showLocal: Boolean,
        showRemote: Boolean,
        sortOption: CollectionSortOption,
    ): Flow<PagingData<CollectionListItemModel>>

    fun getCollection(entityId: String): CollectionListItemModel

    fun insertLocal(
        collection: CollectionListItemModel,
    )

    suspend fun deleteFromCollection(
        collectionId: String,
        entityId: String,
        entityName: String,
    ): ActionableResult

    suspend fun addToCollection(
        collectionId: String,
        entity: MusicBrainzEntity,
        entityId: String,
    ): ActionableResult
}
