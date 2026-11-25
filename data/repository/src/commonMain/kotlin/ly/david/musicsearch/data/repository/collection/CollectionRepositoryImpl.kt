package ly.david.musicsearch.data.repository.collection

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.musicbrainz.api.CollectionApi
import ly.david.musicsearch.data.repository.internal.paging.BrowseEntityRemoteMediator
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.collection.EditACollectionFeedback
import ly.david.musicsearch.shared.domain.collection.PendingEdit
import ly.david.musicsearch.shared.domain.error.Action
import ly.david.musicsearch.shared.domain.error.ActionableResult
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.resourceUriPlural
import ly.david.musicsearch.shared.domain.paging.CommonPagingConfig
import kotlin.time.Clock

class CollectionRepositoryImpl(
    private val collectionApi: CollectionApi,
    private val collectionDao: CollectionDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val browseEntityCountRepository: BrowseRemoteMetadataRepository,
    private val clock: Clock,
) : CollectionRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun observeAllCollections(
        username: String,
        entityType: MusicBrainzEntityType?,
        query: String,
        showLocal: Boolean,
        showRemote: Boolean,
        sortOption: CollectionSortOption,
        entityIdsToCheckExist: Set<String>,
    ): Flow<PagingData<CollectionListItemModel>> =
        Pager(
            config = CommonPagingConfig.pagingConfig,
            remoteMediator = getRemoteMediator(username).takeIf { username.isNotEmpty() },
            pagingSourceFactory = {
                collectionDao.getAllCollections(
                    entityType = entityType,
                    query = "%$query%",
                    showLocal = showLocal,
                    showRemote = showRemote,
                    sortOption = sortOption,
                    entityIdsToCheckExist = entityIdsToCheckExist,
                )
            },
        ).flow

    private fun getRemoteMediator(username: String) = BrowseEntityRemoteMediator<CollectionListItemModel>(
        getRemoteEntityCount = { getRemoteLinkedEntitiesCountByEntity(username) },
        getLocalEntityCount = { getLocalLinkedEntitiesCountByEntity() },
        deleteLocalEntity = { deleteLinkedEntitiesByEntity() },
        browseLinkedEntitiesAndStore = { offset ->
            browseLinkedEntitiesAndStore(
                username = username,
                nextOffset = offset,
            )
        },
    )

    private suspend fun browseLinkedEntitiesAndStore(
        username: String,
        nextOffset: Int,
    ): Int {
        val response = collectionApi.browseCollectionsByUser(
            username = username,
            offset = nextOffset,
            include = CollectionApi.USER_COLLECTIONS,
        )

        browseRemoteMetadataDao.upsert(
            entityId = username,
            browseEntity = MusicBrainzEntityType.COLLECTION,
            remoteCount = response.count,
            lastUpdated = clock.now(),
        )

        val collectionMusicBrainzModels = response.musicBrainzModels
        collectionDao.insertAllRemote(collectionMusicBrainzModels)

        return collectionMusicBrainzModels.size
    }

    private fun getRemoteLinkedEntitiesCountByEntity(username: String): Int? {
        return browseEntityCountRepository.get(
            entityId = username,
            browseEntityType = MusicBrainzEntityType.COLLECTION,
        )?.remoteCount
    }

    private fun getLocalLinkedEntitiesCountByEntity(): Int {
        return collectionDao.getCountOfRemoteCollections()
    }

    private fun deleteLinkedEntitiesByEntity() {
        browseRemoteMetadataDao.withTransaction {
            browseRemoteMetadataDao.deleteAllBrowseRemoteCountByRemoteCollections()
            collectionDao.deleteMusicBrainzCollections()
        }
    }

    override fun observeCountOfLocalCollections(): Flow<Int> {
        return collectionDao.observeCountOfLocalCollections()
    }

    override fun getCollection(entityId: String): CollectionListItemModel? {
        return collectionDao.getCollection(entityId)
    }

    override fun insertLocal(collection: CollectionListItemModel) {
        collectionDao.insertLocal(collection)
    }

    override fun markDeletedFromCollection(
        collection: CollectionListItemModel,
        collectableIds: List<String>,
    ): Flow<Feedback<EditACollectionFeedback>> = flow {
        collectionEntityDao.markDeletedFromCollection(
            collectionId = collection.id,
            collectableIds = collectableIds,
        )
        emit(
            Feedback.Actionable(
                data = EditACollectionFeedback.Deleting(
                    count = collectableIds.size,
                    collectionName = collection.name,
                ),
                action = Action.Undo,
            ),
        )
    }

    override fun unMarkDeletedFromCollection(collectionId: String) {
        collectionEntityDao.unMarkDeletedFromCollection(collectionId = collectionId)
    }

    override fun deleteAllMarkedForDeletionFromCollection(
        collection: CollectionListItemModel,
    ): Flow<Feedback<EditACollectionFeedback>> = flow {
        val idsMarkedForDeletion = collectionEntityDao.getIdsMarkedForDeletionFromCollection(collection.id)

        if (collection.isRemote) {
            emit(
                Feedback.Loading(
                    EditACollectionFeedback.SyncingWithMusicBrainz,
                    time = clock.now(),
                ),
            )
            try {
                idsMarkedForDeletion.chunked(CollectionApi.MAX_ENTITIES_PER_REQUEST).forEach { chunkedIds ->
                    collectionApi.deleteFromCollection(
                        collectionId = collection.id,
                        resourceUriPlural = collection.entity.resourceUriPlural,
                        mbids = chunkedIds.toSet(),
                    )
                }
            } catch (ex: HandledException) {
                emit(
                    Feedback.Error(
                        data = EditACollectionFeedback.FailedToDelete(
                            collectionName = collection.name,
                            errorMessage = ex.userMessage,
                        ),
                        action = Action.Login.takeIf { ex.errorResolution == ErrorResolution.Login },
                        errorResolution = ex.errorResolution,
                        time = clock.now(),
                    ),
                )
                return@flow
            }
        }

        collectionEntityDao.deleteAllMarkedForDeletionFromCollection(collectionId = collection.id)
        emit(
            Feedback.Success(
                data = EditACollectionFeedback.DeletedFromCollection(
                    count = idsMarkedForDeletion.size,
                    collectionName = collection.name,
                ),
                time = clock.now(),
            ),
        )
    }

    override fun deleteFromCollectionImmediately(
        collection: CollectionListItemModel,
        collectableIds: List<String>,
    ): Flow<Feedback<EditACollectionFeedback>> = flow {
        markDeletedFromCollection(
            collection = collection,
            collectableIds = collectableIds,
        ).collect { _ ->
            // ignore
        }

        emitAll(deleteAllMarkedForDeletionFromCollection(collection))
    }

    override fun addToCollection(
        collection: CollectionListItemModel,
        entityType: MusicBrainzEntityType,
        collectableIds: List<String>,
    ): Flow<Feedback<EditACollectionFeedback>> = flow {
        if (collection.isRemote) {
            emit(
                Feedback.Loading(
                    data = EditACollectionFeedback.SyncingWithMusicBrainz,
                    time = clock.now(),
                ),
            )
            try {
                collectableIds.chunked(CollectionApi.MAX_ENTITIES_PER_REQUEST).forEach { chunkedIds ->
                    collectionApi.addToCollection(
                        collectionId = collection.id,
                        resourceUriPlural = entityType.resourceUriPlural,
                        mbids = chunkedIds,
                    )
                }
            } catch (ex: HandledException) {
                emit(
                    Feedback.Error(
                        data = EditACollectionFeedback.FailedToAdd(
                            collectionName = collection.name,
                            errorMessage = ex.userMessage,
                        ),
                        action = Action.Login.takeIf { ex.errorResolution == ErrorResolution.Login },
                        errorResolution = ex.errorResolution,
                        time = clock.now(),
                    ),
                )
                return@flow
            }
        }

        val feedback: EditACollectionFeedback = insertAndGetResult(
            collectionId = collection.id,
            entityIds = collectableIds,
            collection = collection,
        )
        emit(
            Feedback.Success(
                data = feedback,
                time = clock.now(),
            ),
        )
    }

    private fun insertAndGetResult(
        collectionId: String,
        entityIds: List<String>,
        collection: CollectionListItemModel,
    ): EditACollectionFeedback {
        val insertions = collectionEntityDao.addAllToCollection(
            collectionId = collectionId,
            entityIds = entityIds,
        ).toInt()

        val data: EditACollectionFeedback = when {
            insertions == 0 -> EditACollectionFeedback.AlreadyIn(
                collectionName = collection.name,
            )

            entityIds.size == 1 -> EditACollectionFeedback.AddedOne(
                collectionName = collection.name,
            )

            insertions == entityIds.size -> EditACollectionFeedback.AddedMultipleToCollection(
                newInsertions = insertions,
                collectionName = collection.name,
            )

            else -> EditACollectionFeedback.AddedMultipleToCollectionSomeAlreadyAdded(
                newInsertions = insertions,
                collectionName = collection.name,
                countAlreadyAdded = entityIds.size - insertions,
            )
        }
        return data
    }

    override fun applyAllEdits(
        pendingEdits: Map<String, CollectionListItemModel>,
        entityType: MusicBrainzEntityType,
        collectableIds: List<String>,
    ): Flow<Feedback<EditACollectionFeedback>> = flow {
        val hasRemote = pendingEdits.values.any { it.isRemote }
        emit(
            Feedback.Loading(
                data = if (hasRemote) {
                    EditACollectionFeedback.SyncingWithMusicBrainz
                } else {
                    EditACollectionFeedback.Loading
                },
                time = clock.now(),
            ),
        )

        val errorFeedbackList: MutableList<Feedback<EditACollectionFeedback>> = mutableListOf()
        val insertionFeedbackList: MutableList<EditACollectionFeedback> = mutableListOf()
        val deletionFeedbackList: MutableList<EditACollectionFeedback> = mutableListOf()

        fun record(feedback: Feedback<EditACollectionFeedback>) {
            when (feedback) {
                is Feedback.Actionable<*>,
                is Feedback.Loading<*>,
                -> {
                    // ignore
                }

                is Feedback.Error<*> -> {
                    errorFeedbackList.add(feedback)
                }

                is Feedback.Success<*> -> {
                    when (val data = feedback.data) {
                        is EditACollectionFeedback.AddedMultipleToCollection,
                        is EditACollectionFeedback.AddedMultipleToCollectionSomeAlreadyAdded,
                        is EditACollectionFeedback.AddedOne,
                        -> {
                            insertionFeedbackList.add(data)
                        }

                        is EditACollectionFeedback.DeletedFromCollection -> {
                            deletionFeedbackList.add(data)
                        }

                        is EditACollectionFeedback.AlreadyIn,
                        is EditACollectionFeedback.Deleting,
                        EditACollectionFeedback.DoesNotExist,
                        is EditACollectionFeedback.EditedMultipleCollections,
                        is EditACollectionFeedback.FailedToAdd,
                        is EditACollectionFeedback.FailedToDelete,
                        EditACollectionFeedback.Loading,
                        EditACollectionFeedback.SyncingWithMusicBrainz,
                        is EditACollectionFeedback.AddedToMultipleCollections,
                        is EditACollectionFeedback.DeletedFromMultipleCollections,
                        -> {
                            // ignore
                        }
                    }
                }
            }
        }

        pendingEdits.values.toList().forEach { collectionWithPendingEdit ->
            when (collectionWithPendingEdit.pendingEdit) {
                PendingEdit.Add ->
                    addToCollection(
                        collection = collectionWithPendingEdit,
                        entityType = entityType,
                        collectableIds = collectableIds,
                    ).collect { feedback ->
                        record(feedback)
                    }

                // This immediately deletes. Since we pop bottom sheet after applying changes,
                // I don't think it would be simple to offer an undo without forcing all call-sites to implement
                // undo like they do for onLoginClick. Fortunately, selection is kept after popping,
                // so you can simply edit again and add the recently deleted items back.
                PendingEdit.Remove -> {
                    deleteFromCollectionImmediately(
                        collection = collectionWithPendingEdit,
                        collectableIds = collectableIds,
                    ).collect { feedback ->
                        record(feedback)
                    }
                }

                null -> {
                    // No-op.
                }
            }
        }

        emit(
            getSummarizedFeedback(
                errorFeedbackList = errorFeedbackList,
                insertionFeedbackList = insertionFeedbackList,
                deletionFeedbackList = deletionFeedbackList,
                collectableIds = collectableIds,
            ),
        )
    }

    private fun getSummarizedFeedback(
        errorFeedbackList: MutableList<Feedback<EditACollectionFeedback>>,
        insertionFeedbackList: MutableList<EditACollectionFeedback>,
        deletionFeedbackList: MutableList<EditACollectionFeedback>,
        collectableIds: List<String>,
    ): Feedback<EditACollectionFeedback> {
        val firstError = errorFeedbackList.firstOrNull { it is Feedback.Error }
        return if (firstError == null) {
            val feedback: EditACollectionFeedback
            val insertions = insertionFeedbackList.size
            val deletions = deletionFeedbackList.size
            feedback = when {
                insertions == 1 && deletions == 0 -> {
                    insertionFeedbackList.first()
                }

                insertions == 0 && deletions == 1 -> {
                    deletionFeedbackList.first()
                }

                insertions > 1 && deletions == 0 -> {
                    EditACollectionFeedback.AddedToMultipleCollections(
                        count = collectableIds.size,
                        countCollectionsInsertedTo = insertions,
                    )
                }

                insertions == 0 && deletions > 1 -> {
                    EditACollectionFeedback.DeletedFromMultipleCollections(
                        count = collectableIds.size,
                        countCollectionsDeletedFrom = deletions,
                    )
                }

                else -> {
                    EditACollectionFeedback.EditedMultipleCollections(
                        count = collectableIds.size,
                        countCollectionsInsertedTo = insertions,
                        countCollectionsDeletedFrom = deletions,
                    )
                }
            }

            Feedback.Success(
                data = feedback,
                time = clock.now(),
            )
        } else {
            // It's possible to offer login due to showEditCollectionSheet handling it
            // Local edits will succeed even though remote edits failed. This doesn't let the user know.
            firstError
        }
    }

    override fun markDeletedCollections(
        collectionIds: List<String>,
    ): ActionableResult {
        collectionDao.markDeletedCollections(
            collectionIds = collectionIds,
        )
        val message = if (collectionIds.size == 1) {
            "Deleting ${collectionIds.size} collection."
        } else {
            "Deleting ${collectionIds.size} collections."
        }
        return ActionableResult(
            message = message,
            action = Action.Undo,
        )
    }

    override fun unMarkDeletedCollections() {
        collectionDao.unMarkDeletedCollections()
    }

    override suspend fun deleteCollectionsMarkedForDeletion(): ActionableResult {
        collectionDao.deleteCollectionsMarkedForDeletion()
        return ActionableResult(message = "Deleted selected collections.")
    }

    // Seems like this could just come from details query
    override fun observeEntityIsInACollection(
        entityId: String,
    ): Flow<Boolean> {
        return collectionEntityDao.entityIsInACollection(entityId)
    }
}
