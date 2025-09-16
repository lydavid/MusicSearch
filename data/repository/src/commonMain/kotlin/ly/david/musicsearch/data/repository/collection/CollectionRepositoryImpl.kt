package ly.david.musicsearch.data.repository.collection

import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.musicbrainz.api.CollectionApi
import ly.david.musicsearch.data.repository.internal.paging.BrowseEntityRemoteMediator
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.error.Action
import ly.david.musicsearch.shared.domain.error.ActionableResult
import ly.david.musicsearch.shared.domain.error.ErrorResolution
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
) : CollectionRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun observeAllCollections(
        username: String,
        entity: MusicBrainzEntityType?,
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
                    entity = entity,
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
            lastUpdated = Clock.System.now(),
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
        collectableIds: Set<String>,
    ): ActionableResult {
        collectionEntityDao.markDeletedFromCollection(
            collectionId = collection.id,
            collectableIds = collectableIds,
        )
        return ActionableResult(
            message = "Deleting ${collectableIds.size} from ${collection.name}.",
            action = Action.Undo,
        )
    }

    override fun unMarkDeletedFromCollection(collectionId: String) {
        collectionEntityDao.unMarkDeletedFromCollection(collectionId = collectionId)
    }

    override suspend fun deleteFromCollection(collection: CollectionListItemModel): ActionableResult? {
        val idsMarkedForDeletion = collectionEntityDao.getIdsMarkedForDeletionFromCollection(collection.id)
        if (collection.isRemote) {
            try {
                // TODO: handle deleting more than 400 items at a time
                //  https://musicbrainz.org/doc/MusicBrainz_API#collections
                collectionApi.deleteFromCollection(
                    collectionId = collection.id,
                    resourceUriPlural = collection.entity.resourceUriPlural,
                    mbids = idsMarkedForDeletion,
                )
            } catch (ex: HandledException) {
                val userFacingError = "Failed to delete from collection ${collection.name}. ${ex.userMessage}"
                return ActionableResult(
                    message = userFacingError,
                    action = Action.Login.takeIf { ex.errorResolution == ErrorResolution.Login },
                    errorResolution = ex.errorResolution,
                )
            }
        }
        collectionEntityDao.deleteFromCollection(collectionId = collection.id)
        return ActionableResult(
            message = "Deleted ${idsMarkedForDeletion.size} from ${collection.name}.",
            action = null,
        )
    }

    override suspend fun addToCollection(
        collectionId: String,
        entity: MusicBrainzEntityType,
        entityIds: Set<String>,
    ): ActionableResult {
        val collection = collectionDao.getCollection(collectionId) ?: return ActionableResult("Does not exist")

        var result = ActionableResult()
        if (collection.isRemote) {
            try {
                // TODO: support adding more than 16KB worth of items at a time
                collectionApi.addToCollection(
                    collectionId = collectionId,
                    resourceUriPlural = entity.resourceUriPlural,
                    mbids = entityIds,
                )
            } catch (ex: HandledException) {
                val userFacingError = "Failed to add to ${collection.name}. ${ex.userMessage}"
                return ActionableResult(
                    message = userFacingError,
                    action = Action.Login.takeIf { ex.errorResolution == ErrorResolution.Login },
                    errorResolution = ex.errorResolution,
                )
            }
        }

        collectionEntityDao.withTransaction {
            val insertions = collectionEntityDao.addAllToCollection(
                collectionId = collectionId,
                entityIds = entityIds.toList(),
            )

            result = ActionableResult(
                message = when {
                    insertions == 0L -> "Already in ${collection.name}."
                    entityIds.size == 1 -> "Added to ${collection.name}."
                    else -> "Added ${insertions.toInt()} to ${collection.name}."
                },
            )
        }

        return result
    }

    override fun markDeletedCollections(
        collectionIds: Set<String>,
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
