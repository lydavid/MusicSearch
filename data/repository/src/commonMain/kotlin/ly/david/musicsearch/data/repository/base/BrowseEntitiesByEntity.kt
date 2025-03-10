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
        entityId: String?,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): Flow<PagingData<LI>> {
        val remoteMediator = if (entityId != null && entity != null) {
            getRemoteMediator(
                entityId = entityId,
                entity = entity,
            )
        } else {
            null
        }
        return Pager(
            config = CommonPagingConfig.pagingConfig,
            remoteMediator = remoteMediator.takeIf { listFilters.isRemote },
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
        entityId: String?,
        entity: MusicBrainzEntity?,
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

        val insertedCount = browseEntityCountDao.withTransactionWithResult {
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

        return insertedCount
    }

    abstract suspend fun browseEntities(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): B

    // TODO: return the size of musicBrainzModels in repository layer
    //  then each dao insert doesn't need to return how many it inserted
    //  this makes more sense since we're going back to insert or ignore
    //  When paginating, we don't want to sometimes get 99 inserted instead of 100
    //  just because we've seen a model before
    /**
     * Returns the amount of [musicBrainzModels] that have been inserted.
     */
    abstract fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<MB>,
    ): Int

    protected fun browseEntitiesNotSupported(entity: MusicBrainzEntity?) =
        "${browseEntity.resourceUriPlural} by $entity not supported."
}
