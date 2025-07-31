package ly.david.musicsearch.data.repository.base

import androidx.paging.Pager
import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.musicbrainz.api.Browsable
import ly.david.musicsearch.data.musicbrainz.models.core.MusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.paging.BrowseEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.resourceUriPlural

abstract class BrowseEntities<
    LI : ListItemModel,
    MB : MusicBrainzNetworkModel,
    B : Browsable<MB>,
    >(
    val browseEntity: MusicBrainzEntity,
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val aliasDao: AliasDao,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun observeEntities(
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
            deleteEntityLinksByEntity(
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
        browseRemoteMetadataDao.get(
            entityId = entityId,
            browseEntity = browseEntity,
        )?.remoteCount

    abstract fun getLocalLinkedEntitiesCountByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ): Int

    abstract fun deleteEntityLinksByEntity(
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
        val response = browseEntitiesByEntity(
            entityId = entityId,
            entity = entity,
            offset = nextOffset,
        )
        val musicBrainzModels = response.musicBrainzModels

        browseRemoteMetadataDao.withTransaction {
            browseRemoteMetadataDao.upsert(
                entityId = entityId,
                browseEntity = browseEntity,
                remoteCount = response.count,
                lastUpdated = Clock.System.now(),
            )

            // Make sure to insert the entities before inserting the aliases.
            insertAll(
                entityId = entityId,
                entity = entity,
                musicBrainzModels = musicBrainzModels,
            )

            aliasDao.insertAll(
                musicBrainzNetworkModels = musicBrainzModels,
            )
        }

        return musicBrainzModels.size
    }

    abstract suspend fun browseEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): B

    abstract fun insertAll(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<MB>,
    )

    protected fun browseEntitiesNotSupported(entity: MusicBrainzEntity?) =
        "${browseEntity.resourceUriPlural} by $entity not supported."
}
