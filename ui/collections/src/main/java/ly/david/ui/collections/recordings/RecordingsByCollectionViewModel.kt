package ly.david.ui.collections.recordings

import androidx.paging.PagingSource
import ly.david.data.musicbrainz.RecordingMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseRecordingsResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.listitem.RecordingListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.recording.RecordingsPagedList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class RecordingsByCollectionViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val recordingDao: RecordingDao,
    pagedList: RecordingsPagedList,
) : BrowseEntitiesByEntityViewModel<RecordingListItemModel, RecordingMusicBrainzModel, BrowseRecordingsResponse>(
    byEntity = MusicBrainzEntity.RECORDING,
    browseEntityCountDao = browseEntityCountDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseRecordingsResponse {
        return musicBrainzApi.browseRecordingsByCollection(
            collectionId = entityId,
            offset = offset,
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        musicBrainzModels: List<RecordingMusicBrainzModel>,
    ) {
        collectionEntityDao.withTransaction {
            recordingDao.insertAll(musicBrainzModels)
            collectionEntityDao.insertAll(
                collectionId = entityId,
                entityIds = musicBrainzModels.map { recording -> recording.id },
            )
        }
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(entityId)
            browseEntityCountDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RECORDING)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, RecordingListItemModel> =
        collectionEntityDao.getRecordingsByCollection(
            collectionId = entityId,
            query = "%$query%",
        )
}
