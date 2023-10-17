package ly.david.musicsearch.data.repository.work

import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.data.musicbrainz.WorkMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseWorksResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.WorkListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.data.repository.base.BrowseEntitiesByEntity
import ly.david.musicsearch.domain.work.WorksByEntityRepository

class WorksByEntityRepositoryImpl(
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val workDao: WorkDao,
    private val musicBrainzApi: MusicBrainzApi,
) : WorksByEntityRepository,
    BrowseEntitiesByEntity<WorkListItemModel, WorkMusicBrainzModel, BrowseWorksResponse>(
        browseEntity = MusicBrainzEntity.WORK,
        browseEntityCountDao = browseEntityCountDao,
    ) {

    override fun observeWorksByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): Flow<PagingData<WorkListItemModel>> {
        return observeEntitiesByEntity(
            entityId = entityId,
            entity = entity,
            listFilters = listFilters,
        )
    }

    override fun deleteLinkedEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ) {
        browseEntityCountDao.withTransaction {
            browseEntityCountDao.deleteBrowseEntityCountByEntity(
                entityId = entityId,
                browseEntity = browseEntity,
            )

            when (entity) {
                MusicBrainzEntity.COLLECTION -> {
                    collectionEntityDao.deleteAllFromCollection(entityId)
                }

                else -> error(browseEntitiesNotSupported(entity))
            }
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): PagingSource<Int, WorkListItemModel> {
        return when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.getWorksByCollection(
                    collectionId = entityId,
                    query = listFilters.query,
                )
            }

            else -> error(browseEntitiesNotSupported(entity))
        }
    }

    override suspend fun browseEntities(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseWorksResponse {
        return when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                musicBrainzApi.browseWorksByCollection(
                    collectionId = entityId,
                    offset = offset,
                )
            }

            else -> error(browseEntitiesNotSupported(entity))
        }
    }

    override fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<WorkMusicBrainzModel>,
    ) {
        workDao.insertAll(musicBrainzModels)
        when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.insertAll(
                    collectionId = entityId,
                    entityIds = musicBrainzModels.map { work -> work.id },
                )
            }

            else -> {
                error(browseEntitiesNotSupported(entity))
            }
        }
    }
}
