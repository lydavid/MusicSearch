package ly.david.musicsearch.shared.domain.collection

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.error.ActionableResult
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable
import ly.david.musicsearch.shared.domain.parcelize.Parcelize

interface CollectionRepository {
    fun observeAllCollections(
        username: String,
        entity: MusicBrainzEntityType?,
        query: String,
        showLocal: Boolean,
        showRemote: Boolean,
        sortOption: CollectionSortOption,
        entityIdToCheckExists: String? = null,
    ): Flow<PagingData<CollectionListItemModel>>

    fun observeCountOfLocalCollections(): Flow<Int>

    fun getCollection(entityId: String): CollectionListItemModel?

    fun insertLocal(
        collection: CollectionListItemModel,
    )

    fun markDeletedFromCollection(
        collection: CollectionListItemModel,
        collectableIds: List<String>,
    ): Flow<Feedback<CollectionFeedback>>

    fun unMarkDeletedFromCollection(
        collectionId: String,
    )

    suspend fun deleteFromCollection(
        collection: CollectionListItemModel,
    ): Flow<Feedback<CollectionFeedback>>

    suspend fun addToCollection(
        collectionId: String,
        entityType: MusicBrainzEntityType,
        entityIds: List<String>,
    ): ActionableResult

    fun markDeletedCollections(
        collectionIds: List<String>,
    ): ActionableResult

    fun unMarkDeletedCollections()

    suspend fun deleteCollectionsMarkedForDeletion(): ActionableResult

    fun observeEntityIsInACollection(
        entityId: String,
    ): Flow<Boolean>
}

@Parcelize
sealed interface CollectionFeedback : CommonParcelable {
    data object Loading : CollectionFeedback
    data class Deleting(val count: Int, val collectionName: String) : CollectionFeedback
    data class Deleted(val count: Int, val collectionName: String) : CollectionFeedback
    data class Failed(val collectionName: String, val errorMessage: String) : CollectionFeedback
}
