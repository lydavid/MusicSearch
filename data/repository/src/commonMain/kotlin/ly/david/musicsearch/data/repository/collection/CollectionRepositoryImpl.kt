package ly.david.musicsearch.data.repository.collection

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
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
        entityIdToCheckExists: String?,
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
                    entityIdToCheckExists = entityIdToCheckExists,
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
            entity = MusicBrainzEntityType.COLLECTION,
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

    override suspend fun deleteFromCollection(
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

        collectionEntityDao.deleteFromCollection(collectionId = collection.id)
        emit(
            Feedback.Success(
                data = EditACollectionFeedback.Deleted(
                    count = idsMarkedForDeletion.size,
                    collectionName = collection.name,
                ),
                time = clock.now(),
            ),
        )
    }

    override suspend fun addToCollection(
        collectionId: String,
        entityType: MusicBrainzEntityType,
        entityIds: List<String>,
    ): Flow<Feedback<EditACollectionFeedback>> = flow {
        val collection = collectionDao.getCollection(collectionId)
        if (collection == null) {
            emit(
                Feedback.Error(
                    data = EditACollectionFeedback.DoesNotExist,
                    errorResolution = ErrorResolution.None,
                    time = clock.now(),
                ),
            )
            return@flow
        }

        if (collection.isRemote) {
            emit(
                Feedback.Loading(
                    data = EditACollectionFeedback.SyncingWithMusicBrainz,
                    time = clock.now(),
                ),
            )
            try {
                entityIds.chunked(CollectionApi.MAX_ENTITIES_PER_REQUEST).forEach { chunkedIds ->
                    collectionApi.addToCollection(
                        collectionId = collectionId,
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
            collectionId = collectionId,
            entityIds = entityIds,
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

            insertions == entityIds.size -> EditACollectionFeedback.AddedMultiple(
                newInsertions = insertions,
                collectionName = collection.name,
            )

            else -> EditACollectionFeedback.AddedMultipleSomeAlreadyAdded(
                newInsertions = insertions,
                collectionName = collection.name,
                countAlreadyAdded = entityIds.size - insertions,
            )
        }
        return data
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

    override fun observeEntityIsInACollection(
        entityId: String,
    ): Flow<Boolean> {
        return collectionEntityDao.entityIsInACollection(entityId)
    }
}
