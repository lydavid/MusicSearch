package ly.david.musicsearch.data.repository.recording

import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.data.musicbrainz.RecordingMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseRecordingsResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.RecordingListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RecordingWorkDao
import ly.david.musicsearch.data.repository.base.BrowseEntitiesByEntity
import ly.david.musicsearch.domain.recording.RecordingsByEntityRepository

class RecordingsByEntityRepositoryImpl(
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val recordingDao: RecordingDao,
    private val recordingWorkDao: RecordingWorkDao,
    private val musicBrainzApi: MusicBrainzApi,
) : RecordingsByEntityRepository,
    BrowseEntitiesByEntity<RecordingListItemModel, RecordingMusicBrainzModel, BrowseRecordingsResponse>(
        browseEntity = MusicBrainzEntity.RECORDING,
        browseEntityCountDao = browseEntityCountDao,
    ) {

    override fun observeRecordingsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): Flow<PagingData<RecordingListItemModel>> {
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

                MusicBrainzEntity.WORK -> {
                    recordingWorkDao.deleteRecordingsByWork(entityId)
                }

                else -> error(browseEntitiesNotSupported(entity))
            }
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): PagingSource<Int, RecordingListItemModel> {
        return when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.getRecordingsByCollection(
                    collectionId = entityId,
                    query = listFilters.query,
                )
            }

            MusicBrainzEntity.WORK -> {
                recordingWorkDao.getRecordingsByWork(
                    workId = entityId,
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
    ): BrowseRecordingsResponse {
        return when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                musicBrainzApi.browseRecordingsByCollection(
                    collectionId = entityId,
                    offset = offset,
                )
            }

            MusicBrainzEntity.WORK -> {
                musicBrainzApi.browseRecordingsByWork(
                    workId = entityId,
                    offset = offset,
                )
            }

            else -> error(browseEntitiesNotSupported(entity))
        }
    }

    override fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<RecordingMusicBrainzModel>,
    ) {
        recordingDao.insertAll(musicBrainzModels)
        when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.insertAll(
                    collectionId = entityId,
                    entityIds = musicBrainzModels.map { recording -> recording.id },
                )
            }

            MusicBrainzEntity.WORK -> {
                recordingWorkDao.insertAll(
                    workId = entityId,
                    recordingIds = musicBrainzModels.map { recording -> recording.id },
                )
            }

            else -> {
                error(browseEntitiesNotSupported(entity))
            }
        }
    }
}
