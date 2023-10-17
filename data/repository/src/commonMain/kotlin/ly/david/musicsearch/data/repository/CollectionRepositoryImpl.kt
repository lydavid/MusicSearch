package ly.david.musicsearch.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.data.musicbrainz.api.CollectionApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.repository.internal.paging.BrowseEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.domain.browse.BrowseEntityCountRepository
import ly.david.musicsearch.domain.collection.CollectionRepository
import lydavidmusicsearchdatadatabase.Browse_entity_count

class CollectionRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionDao: CollectionDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val browseEntityCountRepository: BrowseEntityCountRepository,
) : CollectionRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun observeAllCollections(
        username: String,
        showLocal: Boolean,
        showRemote: Boolean,
        query: String,
        entity: MusicBrainzEntity?,
    ): Flow<PagingData<CollectionListItemModel>> =
        Pager(
            config = CommonPagingConfig.pagingConfig,
            remoteMediator = getRemoteMediator(username).takeIf { username.isNotEmpty() },
            pagingSourceFactory = {
                collectionDao.getAllCollections(
                    showLocal = showLocal,
                    showRemote = showRemote,
                    query = "%$query%",
                    entity = entity,
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
}
