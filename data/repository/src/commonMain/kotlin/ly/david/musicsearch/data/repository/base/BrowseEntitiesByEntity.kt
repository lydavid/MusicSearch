package ly.david.musicsearch.data.repository.base

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.musicbrainz.models.core.MusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.Browsable
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.network.resourceUriPlural
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.repository.internal.paging.BrowseEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import lydavidmusicsearchdatadatabase.Browse_entity_count

abstract class BrowseEntitiesByEntity<
    LI : ListItemModel,
    MB : MusicBrainzModel,
    B : Browsable<MB>,
    >(
    val browseEntity: MusicBrainzEntity,
    private val browseEntityCountDao: BrowseEntityCountDao,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun observeEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): Flow<PagingData<LI>> {
        return Pager(
            config = CommonPagingConfig.pagingConfig,
            remoteMediator = getRemoteMediator(
                entityId = entityId,
                entity = entity,
            ).takeIf { listFilters.isRemote },
            pagingSourceFactory = {
                getLinkedEntitiesPagingSource(
                    entityId = entityId,
                    entity = entity,
                    listFilters = listFilters,
                )
            },
        ).flow
    }

    private fun getRemoteMediator(
        entityId: String,
        entity: MusicBrainzEntity,
    ) = BrowseEntityRemoteMediator<LI>(
        getRemoteEntityCount = { getRemoteLinkedEntitiesCountByEntity(entityId) },
        getLocalEntityCount = { getLocalLinkedEntitiesCountByEntity(entityId) },
        deleteLocalEntity = {
            deleteLinkedEntitiesByEntity(
                entityId = entityId,
                entity = entity,
            )
        },
        browseEntity = { offset ->
            browseLinkedEntitiesAndStore(
                entityId = entityId,
                entity = entity,
                nextOffset = offset,
            )
        },
    )

    private fun getRemoteLinkedEntitiesCountByEntity(entityId: String): Int? =
        browseEntityCountDao.getBrowseEntityCount(
            entityId = entityId,
            browseEntity = browseEntity,
        )?.remoteCount

    private fun getLocalLinkedEntitiesCountByEntity(entityId: String): Int =
        browseEntityCountDao.getBrowseEntityCount(
            entityId = entityId,
            browseEntity = browseEntity,
        )?.localCount ?: 0

    abstract fun deleteLinkedEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    )

    abstract fun getLinkedEntitiesPagingSource(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): PagingSource<Int, LI>

    private suspend fun browseLinkedEntitiesAndStore(
        entityId: String,
        entity: MusicBrainzEntity,
        nextOffset: Int,
    ): Int {
        val response = browseEntities(
            entityId = entityId,
            entity = entity,
            offset = nextOffset,
        )

        if (response.offset == 0) {
            browseEntityCountDao.insert(
                browseEntityCount = Browse_entity_count(
                    entity_id = entityId,
                    browse_entity = browseEntity,
                    local_count = response.musicBrainzModels.size,
                    remote_count = response.count,
                ),
            )
        } else {
            browseEntityCountDao.incrementLocalCountForEntity(
                entityId = entityId,
                browseEntity = browseEntity,
                additionalOffset = response.musicBrainzModels.size,
            )
        }

        val musicBrainzModels = response.musicBrainzModels
        insertAllLinkingModels(
            entityId = entityId,
            entity = entity,
            musicBrainzModels = musicBrainzModels,
        )

        return musicBrainzModels.size
    }

    abstract suspend fun browseEntities(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): B

    abstract fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<MB>,
    )

    protected fun browseEntitiesNotSupported(entity: MusicBrainzEntity) =
        "${browseEntity.resourceUriPlural} by $entity not supported."
}
