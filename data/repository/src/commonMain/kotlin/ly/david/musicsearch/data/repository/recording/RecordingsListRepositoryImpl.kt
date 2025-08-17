package ly.david.musicsearch.data.repository.recording

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseRecordingsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.BrowseEntities
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.recording.RecordingsListRepository

class RecordingsListRepositoryImpl(
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val recordingDao: RecordingDao,
    private val browseApi: BrowseApi,
    aliasDao: AliasDao,
) : RecordingsListRepository,
    BrowseEntities<RecordingListItemModel, RecordingMusicBrainzNetworkModel, BrowseRecordingsResponse>(
        browseEntity = MusicBrainzEntityType.RECORDING,
        browseRemoteMetadataDao = browseRemoteMetadataDao,
        aliasDao = aliasDao,
    ) {

    override fun observeRecordings(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<RecordingListItemModel>> {
        return observeEntities(
            browseMethod = browseMethod,
            listFilters = listFilters,
        )
    }

    override fun getLinkedEntitiesPagingSource(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): PagingSource<Int, RecordingListItemModel> {
        return recordingDao.getRecordings(
            browseMethod = browseMethod,
            query = listFilters.query,
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
                    recordingDao.deleteRecordingLinksByEntity(entityId)
                }
            }
        }
    }

    override suspend fun browseEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntityType,
        offset: Int,
    ): BrowseRecordingsResponse {
        return browseApi.browseRecordingsByEntity(
            entityId = entityId,
            entity = entity,
            offset = offset,
        )
    }

    override fun insertAll(
        entityId: String,
        entity: MusicBrainzEntityType,
        musicBrainzModels: List<RecordingMusicBrainzNetworkModel>,
    ) {
        recordingDao.insertAll(musicBrainzModels)
        when (entity) {
            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.addAllToCollection(
                    collectionId = entityId,
                    entityIds = musicBrainzModels.map { recording -> recording.id },
                )
            }

            else -> {
                recordingDao.insertRecordingsByEntity(
                    entityId = entityId,
                    recordingIds = musicBrainzModels.map { recording -> recording.id },
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
                recordingDao.getCountOfRecordingsByEntity(entityId)
            }
        }
    }
}
