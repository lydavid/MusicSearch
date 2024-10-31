package ly.david.musicsearch.data.repository.collection

import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.INSERTION_FAILED_DUE_TO_CONFLICT
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.musicbrainz.api.CollectionApi
import ly.david.musicsearch.data.repository.internal.paging.BrowseEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.shared.domain.browse.BrowseEntityCountRepository
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.error.ActionableResult
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.resourceUriPlural
import lydavidmusicsearchdatadatabase.Browse_entity_count

class CollectionRepositoryImpl(
    private val collectionApi: CollectionApi,
    private val collectionDao: CollectionDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val browseEntityCountRepository: BrowseEntityCountRepository,
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
        val response = collectionApi.browseCollectionsByUser(
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
}
