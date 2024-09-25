package ly.david.musicsearch.data.repository

import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.ActionableResult
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.resourceUriPlural
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.data.database.INSERTION_FAILED_DUE_TO_CONFLICT
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.musicbrainz.api.CollectionApi
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.repository.internal.paging.BrowseEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.shared.domain.browse.BrowseEntityCountRepository
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import lydavidmusicsearchdatadatabase.Browse_entity_count

class CollectionRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionDao: CollectionDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val browseEntityCountRepository: BrowseEntityCountRepository,
    private val logger: Logger,
) : CollectionRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun observeAllCollections(
        username: String,
        entity: MusicBrainzEntity?,
        query: String,
        showLocal: Boolean,
        showRemote: Boolean,
        sortOption: CollectionSortOption,
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
                )
            },
        ).flow

    private fun getRemoteMediator(entityId: String) = BrowseEntityRemoteMediator<CollectionListItemModel>(
        getRemoteEntityCount = { getRemoteLinkedEntitiesCountByEntity(entityId) },
        getLocalEntityCount = { getLocalLinkedEntitiesCountByEntity(entityId) },
        deleteLocalEntity = { deleteLinkedEntitiesByEntity() },
        browseEntity = { offset ->
            browseLinkedEntitiesAndStore(
                entityId,
                offset,
            )
        },
    )

    private suspend fun browseLinkedEntitiesAndStore(
        entityId: String,
        nextOffset: Int,
    ): Int {
        val response = musicBrainzApi.browseCollectionsByUser(
            username = entityId,
            offset = nextOffset,
            include = CollectionApi.USER_COLLECTIONS,
        )

        if (response.offset == 0) {
            browseEntityCountDao.insert(
                browseEntityCount = Browse_entity_count(
                    entity_id = entityId,
                    browse_entity = MusicBrainzEntity.COLLECTION,
                    local_count = response.musicBrainzModels.size,
                    remote_count = response.count,
                ),
            )
        } else {
            browseEntityCountDao.incrementLocalCountForEntity(
                entityId = entityId,
                browseEntity = MusicBrainzEntity.COLLECTION,
                additionalOffset = response.musicBrainzModels.size,
            )
        }

        val collectionMusicBrainzModels = response.musicBrainzModels
        collectionDao.insertAllRemote(collectionMusicBrainzModels)

        return collectionMusicBrainzModels.size
    }

    private fun getRemoteLinkedEntitiesCountByEntity(entityId: String): Int? {
        return browseEntityCountRepository.getBrowseEntityCount(
            entityId,
            MusicBrainzEntity.COLLECTION,
        )?.remoteCount
    }

    private fun getLocalLinkedEntitiesCountByEntity(entityId: String): Int {
        return browseEntityCountRepository.getBrowseEntityCount(
            entityId,
            MusicBrainzEntity.COLLECTION,
        )?.localCount ?: 0
    }

    private fun deleteLinkedEntitiesByEntity() {
        browseEntityCountDao.withTransaction {
            browseEntityCountDao.deleteAllBrowseEntityCountByRemoteCollections()
            collectionDao.deleteMusicBrainzCollections()
        }
    }

    override fun getCollection(entityId: String): CollectionListItemModel = collectionDao.getCollection(entityId)

    override fun insertLocal(collection: CollectionListItemModel) {
        collectionDao.insertLocal(collection)
    }

    override suspend fun deleteFromCollection(
        collectionId: String,
        entityId: String,
        entityName: String,
    ): ActionableResult {
        val collection = collectionDao.getCollection(collectionId)
        if (collection.isRemote) {
            try {
                musicBrainzApi.deleteFromCollection(
                    collectionId = collectionId,
                    resourceUriPlural = collection.entity.resourceUriPlural,
                    mbids = entityId,
                )
            } catch (ex: RecoverableNetworkException) {
                val userFacingError = "Failed to delete from remote collection ${collection.name}. Not logged in."
                return ActionableResult(
                    message = userFacingError,
                    actionLabel = "Login",
                )
            }
        }

        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteFromCollection(
                collectionId,
                entityId,
            )
        }
        return ActionableResult("Deleted $entityName from ${collection.name}.")
    }

    override suspend fun addToCollection(
        collectionId: String,
        entity: MusicBrainzEntity,
        entityId: String,
    ): ActionableResult {
        var result = ActionableResult()

        val collection = collectionDao.getCollection(collectionId)
        if (collection.isRemote) {
            try {
                musicBrainzApi.uploadToCollection(
                    collectionId = collectionId,
                    resourceUriPlural = entity.resourceUriPlural,
                    mbids = entityId,
                )
            } catch (ex: RecoverableNetworkException) {
                val userFacingError = "Failed to add to ${collection.name}. Login has expired."
                logger.e(ex)
                return ActionableResult(
                    message = userFacingError,
                    actionLabel = "Login",
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
}
