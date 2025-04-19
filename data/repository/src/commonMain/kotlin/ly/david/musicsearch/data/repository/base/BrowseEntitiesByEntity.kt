package ly.david.musicsearch.data.repository.base

import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.musicbrainz.api.Browsable
import ly.david.musicsearch.data.musicbrainz.models.core.MusicBrainzModel
import ly.david.musicsearch.data.repository.internal.paging.BrowseEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.resourceUriPlural
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
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<LI>> {
        val remoteMediator = if (browseMethod is BrowseMethod.ByEntity) {
            getRemoteMediator(
                entityId = browseMethod.entityId,
                entity = browseMethod.entity,
            )
        } else {
            null
        }
        return Pager(
            config = CommonPagingConfig.pagingConfig,
            remoteMediator = remoteMediator.takeIf { listFilters.isRemote },
            pagingSourceFactory = {
                getLinkedEntitiesPagingSource(
                    browseMethod = browseMethod,
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
        getLocalEntityCount = {
            getLocalLinkedEntitiesCountByEntity(
                entityId = entityId,
                entity = entity,
            )
        },
        deleteLocalEntity = {
            deleteLinkedEntitiesByEntity(
                entityId = entityId,
                entity = entity,
            )
        },
        browseLinkedEntitiesAndStore = { offset ->
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

    abstract fun getLocalLinkedEntitiesCountByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ): Int

    abstract fun deleteLinkedEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    )

    abstract fun getLinkedEntitiesPagingSource(
        browseMethod: BrowseMethod,
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
        val musicBrainzModels = response.musicBrainzModels

        browseEntityCountDao.withTransaction {
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
                browseEntityCountDao.updateBrowseEntityCount(
                    entityId = entityId,
                    browseEntity = browseEntity,
                    additionalOffset = response.musicBrainzModels.size,
                    remoteCount = response.count,
                )
            }

            insertAllLinkingModels(
                entityId = entityId,
                entity = entity,
                musicBrainzModels = musicBrainzModels,
            )
        }

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

    protected fun browseEntitiesNotSupported(entity: MusicBrainzEntity?) =
        "${browseEntity.resourceUriPlural} by $entity not supported."
}
