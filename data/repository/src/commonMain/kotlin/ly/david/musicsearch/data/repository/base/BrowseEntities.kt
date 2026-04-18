package ly.david.musicsearch.data.repository.base

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.musicbrainz.api.Browsable
import ly.david.musicsearch.data.musicbrainz.models.core.MusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.paging.BrowseEntityRemoteMediator
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.resourceUriPlural
import ly.david.musicsearch.shared.domain.paging.CommonPagingConfig
import kotlin.time.Clock
import kotlin.time.Instant

abstract class BrowseEntities<
    LI : ListItemModel,
    MB : MusicBrainzNetworkModel,
    B : Browsable<MB>,
    >(
    val browseEntity: MusicBrainzEntityType,
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val aliasDao: AliasDao,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun observeEntities(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
        now: Instant = Clock.System.now(),
    ): Flow<PagingData<LI>> {
        val remoteMediator = if (browseMethod is BrowseMethod.ByEntity) {
            getRemoteMediator(
                entity = MusicBrainzEntity(
                    id = browseMethod.entityId,
                    type = browseMethod.entityType,
                ),
                now = now,
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
        entity: MusicBrainzEntity,
        now: Instant,
    ) = BrowseEntityRemoteMediator<LI>(
        getRemoteEntityCount = { getRemoteLinkedEntitiesCountByEntity(entity.id) },
        getLocalEntityCount = {
            getLocalLinkedEntitiesCountByEntity(
                entity = entity,
            )
        },
        deleteLocalEntity = {
            deleteEntityLinksByEntity(
                entity = entity,
            )
        },
        browseLinkedEntitiesAndStore = { offset ->
            browseLinkedEntitiesAndStore(
                entity = entity,
                nextOffset = offset,
                now = now,
            )
        },
    )

    private fun getRemoteLinkedEntitiesCountByEntity(entityId: String): Int? =
        browseRemoteMetadataDao.get(
            entityId = entityId,
            browseEntity = browseEntity,
        )?.remoteCount

    abstract fun getLocalLinkedEntitiesCountByEntity(
        entity: MusicBrainzEntity,
    ): Int

    abstract fun deleteEntityLinksByEntity(
        entity: MusicBrainzEntity,
    )

    abstract fun getLinkedEntitiesPagingSource(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): PagingSource<Int, LI>

    private suspend fun browseLinkedEntitiesAndStore(
        entity: MusicBrainzEntity,
        nextOffset: Int,
        now: Instant,
    ): Int {
        val response = browseEntitiesByEntity(
            entity = entity,
            offset = nextOffset,
        )
        val musicBrainzModels = response.musicBrainzModels

        browseRemoteMetadataDao.withTransaction {
            browseRemoteMetadataDao.upsert(
                entityId = entity.id,
                browseEntity = browseEntity,
                remoteCount = response.count,
                lastUpdated = now,
            )

            // Make sure to insert the entities before inserting the aliases.
            insertAll(
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
        entity: MusicBrainzEntity,
        offset: Int,
    ): B

    abstract fun insertAll(
        entity: MusicBrainzEntity,
        musicBrainzModels: List<MB>,
    )

    protected fun browseEntitiesNotSupported(entity: MusicBrainzEntityType?) =
        "${browseEntity.resourceUriPlural} by $entity not supported."
}
