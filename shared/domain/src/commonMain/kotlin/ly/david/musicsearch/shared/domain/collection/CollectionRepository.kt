package ly.david.musicsearch.shared.domain.collection

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.error.ActionableResult
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

interface CollectionRepository {
    fun observeAllCollections(
        username: String,
        entity: MusicBrainzEntity?,
        query: String,
        showLocal: Boolean,
        showRemote: Boolean,
        sortOption: CollectionSortOption,
    ): Flow<PagingData<CollectionListItemModel>>

    fun getCollection(entityId: String): CollectionListItemModel?

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

    suspend fun deleteCollection(
        collectionId: String,
        collectionName: String,
    ): ActionableResult
}
