package ly.david.ui.collections.recordings

import androidx.paging.PagingSource
import ly.david.data.core.RecordingWithArtistCredits
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.listitem.RecordingListItemModel
import ly.david.data.domain.listitem.toRecordingListItemModel
import ly.david.data.musicbrainz.RecordingMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseRecordingsResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
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
) : BrowseEntitiesByEntityViewModel<RecordingWithArtistCredits, RecordingListItemModel, RecordingMusicBrainzModel, BrowseRecordingsResponse>(
    byEntity = MusicBrainzEntity.RECORDING,
    browseEntityCountDao = browseEntityCountDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseRecordingsResponse {
        return musicBrainzApi.browseRecordingsByCollection(
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        musicBrainzModels: List<RecordingMusicBrainzModel>,
    ) {
        recordingDao.insertAll(musicBrainzModels)
        collectionEntityDao.insertAll(
            entityId,
            musicBrainzModels.map { recording -> recording.id },
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        collectionEntityDao.deleteAllFromCollection(entityId)
        browseEntityCountDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RECORDING)
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, RecordingWithArtistCredits> =
        collectionEntityDao.getRecordingsByCollection(
            collectionId = entityId,
            query = "%$query%"
        )

    override fun transformRoomToListItemModel(roomModel: RecordingWithArtistCredits): RecordingListItemModel {
        return roomModel.toRecordingListItemModel()
    }
}
