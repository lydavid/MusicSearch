package ly.david.musicsearch.data.repository.collection

import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import ly.david.musicsearch.data.database.INSERTION_FAILED_DUE_TO_CONFLICT
import ly.david.musicsearch.data.database.dao.BrowseRemoteCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.musicbrainz.api.CollectionApi
import ly.david.musicsearch.data.repository.internal.paging.BrowseEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.error.ActionableResult
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.resourceUriPlural

class CollectionRepositoryImpl(
    private val collectionApi: CollectionApi,
    private val collectionDao: CollectionDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val browseEntityCountDao: BrowseRemoteCountDao,
    private val browseEntityCountRepository: BrowseRemoteMetadataRepository,
) : CollectionRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun observeAllCollections(
        username: String,
        entity: MusicBrainzEntity?,
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

        browseEntityCountDao.upsert(
            entityId = username,
            browseEntity = MusicBrainzEntity.COLLECTION,
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
            entity = MusicBrainzEntity.COLLECTION,
        )?.remoteCount
    }

    private fun getLocalLinkedEntitiesCountByEntity(): Int {
        return collectionDao.getCountOfRemoteCollections()
    }

    private fun deleteLinkedEntitiesByEntity() {
        browseEntityCountDao.withTransaction {
            browseEntityCountDao.deleteAllBrowseRemoteCountByRemoteCollections()
            collectionDao.deleteMusicBrainzCollections()
        }
    }

    override fun getCollection(entityId: String): CollectionListItemModel? {
        return collectionDao.getCollection(entityId)
    }

    override fun insertLocal(collection: CollectionListItemModel) {
        collectionDao.insertLocal(collection)
    }

    override suspend fun deleteFromCollection(
        collectionId: String,
        entityId: String,
        entityName: String,
    ): ActionableResult {
        val collection = collectionDao.getCollection(collectionId) ?: return ActionableResult("Does not exist")
        if (collection.isRemote) {
            try {
                collectionApi.deleteFromCollection(
                    collectionId = collectionId,
                    resourceUriPlural = collection.entity.resourceUriPlural,
                    mbids = entityId,
                )
            } catch (ex: HandledException) {
                val userFacingError = "Failed to delete from collection ${collection.name}. ${ex.userMessage}"
                return ActionableResult(
                    message = userFacingError,
                    actionLabel = "Login".takeIf { ex.errorResolution == ErrorResolution.Login },
                    errorResolution = ex.errorResolution,
                )
            }
        }

        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteFromCollection(
                collectionId = collectionId,
                collectableId = entityId,
            )
        }
        return ActionableResult("Deleted $entityName from ${collection.name}.")
    }

    override suspend fun addToCollection(
        collectionId: String,
        entity: MusicBrainzEntity,
        entityId: String,
    ): ActionableResult {
        val collection = collectionDao.getCollection(collectionId) ?: return ActionableResult("Does not exist")

        var result = ActionableResult()
        if (collection.isRemote) {
            try {
                collectionApi.uploadToCollection(
                    collectionId = collectionId,
                    resourceUriPlural = entity.resourceUriPlural,
                    mbids = entityId,
                )
            } catch (ex: HandledException) {
                val userFacingError = "Failed to add to ${collection.name}. ${ex.userMessage}"
                return ActionableResult(
                    message = userFacingError,
                    actionLabel = "Login".takeIf { ex.errorResolution == ErrorResolution.Login },
                    errorResolution = ex.errorResolution,
                )
            }
        }

        collectionEntityDao.withTransaction {
            val insertedOneEntry = collectionEntityDao.insert(
                collectionId = collectionId,
                entityId = entityId,
            )

            result = if (insertedOneEntry == INSERTION_FAILED_DUE_TO_CONFLICT) {
                ActionableResult("Already in ${collection.name}.")
            } else {
                ActionableResult("Added to ${collection.name}.")
            }
        }

        return result
    }

    override suspend fun deleteCollection(
        collectionId: String,
        collectionName: String,
    ): ActionableResult {
        collectionEntityDao.deleteCollection(
            collectionId,
        )
        return ActionableResult("Deleted $collectionName.")
    }
}
