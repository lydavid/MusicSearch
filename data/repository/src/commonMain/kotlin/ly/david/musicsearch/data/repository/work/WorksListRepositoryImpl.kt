package ly.david.musicsearch.data.repository.work

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseWorksResponse
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.BrowseEntities
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.work.WorksListRepository

class WorksListRepositoryImpl(
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val workDao: WorkDao,
    private val browseApi: BrowseApi,
    private val listenBrainzAuthStore: ListenBrainzAuthStore,
    aliasDao: AliasDao,
) : WorksListRepository,
    BrowseEntities<WorkListItemModel, WorkMusicBrainzNetworkModel, BrowseWorksResponse>(
        browseEntity = MusicBrainzEntityType.WORK,
        browseRemoteMetadataDao = browseRemoteMetadataDao,
        aliasDao = aliasDao,
    ) {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeWorks(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<WorkListItemModel>> {
        return listenBrainzAuthStore.browseUsername.flatMapLatest { username ->
            observeEntities(
                browseMethod = browseMethod,
                listFilters = listFilters.copy(
                    username = username,
                ),
            )
        }
    }

    override fun getLinkedEntitiesPagingSource(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): PagingSource<Int, WorkListItemModel> {
        return workDao.getWorks(
            browseMethod = browseMethod,
            query = listFilters.query,
            username = listFilters.username,
        )
    }

    override fun deleteEntityLinksByEntity(
        entityId: String,
        entity: MusicBrainzEntityType,
    ) {
        browseRemoteMetadataDao.withTransaction {
            browseRemoteMetadataDao.deleteBrowseRemoteCountByEntity(
                entityId = entityId,
                browseEntity = browseEntity,
            )

            when (entity) {
                MusicBrainzEntityType.COLLECTION -> {
                    collectionEntityDao.deleteAllFromCollection(entityId)
                }

                else -> {
                    workDao.deleteWorkLinksByEntity(entityId)
                }
            }
        }
    }

    override suspend fun browseEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntityType,
        offset: Int,
    ): BrowseWorksResponse {
        return browseApi.browseWorksByEntity(
            entityId = entityId,
            entity = entity,
            offset = offset,
        )
    }

    override fun insertAll(
        entityId: String,
        entity: MusicBrainzEntityType,
        musicBrainzModels: List<WorkMusicBrainzNetworkModel>,
    ) {
        workDao.upsertAll(musicBrainzModels)
        when (entity) {
            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.addAllToCollection(
                    collectionId = entityId,
                    entityIds = musicBrainzModels.map { work -> work.id },
                )
            }

            else -> {
                workDao.insertWorksByEntity(
                    entityId = entityId,
                    workIds = musicBrainzModels.map { work -> work.id },
                )
            }
        }
    }

    override fun getLocalLinkedEntitiesCountByEntity(
        entityId: String,
        entity: MusicBrainzEntityType,
    ): Int {
        return when (entity) {
            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.getCountOfEntitiesByCollection(entityId)
            }

            else -> {
                workDao.getCountOfWorksByEntity(entityId)
            }
        }
    }
}
