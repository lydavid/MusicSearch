package ly.david.ui.collections.recordings

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.listitem.RecordingListItemModel
import ly.david.data.domain.listitem.toRecordingListItemModel
import ly.david.data.musicbrainz.MusicBrainzAuthState
import ly.david.data.musicbrainz.getBearerToken
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.network.api.BrowseRecordingsResponse
import ly.david.data.network.api.MusicBrainzApi
import ly.david.data.room.collection.CollectionEntityDao
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.recording.RecordingDao
import ly.david.data.room.recording.RecordingForListItem
import ly.david.data.room.recording.toRoomModel
import ly.david.data.room.relation.RelationDao
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.paging.PagedList

@HiltViewModel
internal class RecordingsByCollectionViewModel @Inject constructor(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val relationDao: RelationDao,
    private val recordingDao: RecordingDao,
    pagedList: PagedList<RecordingForListItem, RecordingListItemModel>,
    private val musicBrainzAuthState: MusicBrainzAuthState,
) : BrowseEntitiesByEntityViewModel<RecordingForListItem, RecordingListItemModel, RecordingMusicBrainzModel, BrowseRecordingsResponse>(
    byEntity = MusicBrainzEntity.RECORDING,
    relationDao = relationDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseRecordingsResponse {
        return musicBrainzApi.browseRecordingsByCollection(
            bearerToken = musicBrainzAuthState.getBearerToken(),
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        musicBrainzModels: List<RecordingMusicBrainzModel>,
    ) {
        recordingDao.insertAll(musicBrainzModels.map { it.toRoomModel() })
        collectionEntityDao.insertAll(
            musicBrainzModels.map { recording ->
                CollectionEntityRoomModel(
                    id = entityId,
                    entityId = recording.id
                )
            }
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(entityId)
            relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RECORDING)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, RecordingForListItem> = when {
        query.isEmpty() -> {
            collectionEntityDao.getRecordingsByCollection(entityId)
        }
        else -> {
            collectionEntityDao.getRecordingsByCollectionFiltered(
                collectionId = entityId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: RecordingForListItem): RecordingListItemModel {
        return roomModel.toRecordingListItemModel()
    }
}
