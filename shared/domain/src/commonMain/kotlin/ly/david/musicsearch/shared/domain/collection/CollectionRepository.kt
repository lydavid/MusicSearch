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
        entityType: MusicBrainzEntityType?,
        query: String,
        showLocal: Boolean,
        showRemote: Boolean,
        sortOption: CollectionSortOption,
        entityIdsToCheckExist: Set<String> = setOf(),
    ): Flow<PagingData<CollectionListItemModel>>

    fun observeCountOfLocalCollections(): Flow<Int>

    fun getCollection(entityId: String): CollectionListItemModel?

    fun insertLocal(
        collection: CollectionListItemModel,
    )

    fun markDeletedFromCollection(
        collection: CollectionListItemModel,
        collectableIds: List<String>,
    ): Flow<Feedback<EditACollectionFeedback>>

    fun unMarkDeletedFromCollection(
        collectionId: String,
    )

    fun deleteAllMarkedForDeletionFromCollection(
        collection: CollectionListItemModel,
    ): Flow<Feedback<EditACollectionFeedback>>

    fun deleteFromCollectionImmediately(
        collection: CollectionListItemModel,
        collectableIds: List<String>,
    ): Flow<Feedback<EditACollectionFeedback>>

    fun addToCollection(
        collection: CollectionListItemModel,
        entityType: MusicBrainzEntityType,
        collectableIds: List<String>,
    ): Flow<Feedback<EditACollectionFeedback>>

    fun applyAllEdits(
        pendingEdits: Map<String, CollectionListItemModel>,
        entityType: MusicBrainzEntityType,
        collectableIds: List<String>,
    ): Flow<Feedback<EditACollectionFeedback>>

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
sealed interface EditACollectionFeedback : CommonParcelable {
    data object SyncingWithMusicBrainz : EditACollectionFeedback

    data object Loading : EditACollectionFeedback

    data class Deleting(
        val count: Int,
        val collectionName: String,
    ) : EditACollectionFeedback

    data class DeletedFromCollection(
        val count: Int,
        val collectionName: String,
    ) : EditACollectionFeedback

    data class FailedToDelete(
        val collectionName: String,
        val errorMessage: String,
    ) : EditACollectionFeedback

    data class FailedToAdd(
        val collectionName: String,
        val errorMessage: String,
    ) : EditACollectionFeedback

    data object DoesNotExist : EditACollectionFeedback

    // should no longer be possible
    data class AlreadyIn(val collectionName: String) : EditACollectionFeedback

    data class AddedOne(val collectionName: String) : EditACollectionFeedback

    data class AddedMultipleToCollection(
        val newInsertions: Int,
        val collectionName: String,
    ) : EditACollectionFeedback

    data class AddedMultipleToCollectionSomeAlreadyAdded(
        val newInsertions: Int,
        val collectionName: String,
        val countAlreadyAdded: Int,
    ) : EditACollectionFeedback

    data class AddedToMultipleCollections(
        val count: Int,
        val countCollectionsInsertedTo: Int,
    ) : EditACollectionFeedback

    data class DeletedFromMultipleCollections(
        val count: Int,
        val countCollectionsDeletedFrom: Int,
    ) : EditACollectionFeedback

    data class EditedMultipleCollections(
        val count: Int,
        val countCollectionsInsertedTo: Int,
        val countCollectionsDeletedFrom: Int,
    ) : EditACollectionFeedback
}
