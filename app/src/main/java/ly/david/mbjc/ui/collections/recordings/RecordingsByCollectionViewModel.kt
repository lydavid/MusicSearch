package ly.david.mbjc.ui.collections.recordings

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.RecordingListItemModel
import ly.david.data.domain.toRecordingListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.network.api.BrowseRecordingsResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.collection.CollectionEntityDao
import ly.david.data.persistence.collection.CollectionEntityRoomModel
import ly.david.data.persistence.recording.RecordingDao
import ly.david.data.persistence.recording.RecordingForListItem
import ly.david.data.persistence.recording.toRoomModel
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.mbjc.ui.common.paging.PagedList

@HiltViewModel
internal class RecordingsByCollectionViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val collectionEntityDao: CollectionEntityDao,
    private val relationDao: RelationDao,
    private val recordingDao: RecordingDao,
    pagedList: PagedList<RecordingForListItem, RecordingListItemModel>,
) : BrowseEntitiesByEntityViewModel
<RecordingForListItem, RecordingListItemModel, RecordingMusicBrainzModel, BrowseRecordingsResponse>(
    byEntity = MusicBrainzResource.RECORDING,
    relationDao = relationDao,
    pagedList = pagedList
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseRecordingsResponse {
        return musicBrainzApiService.browseRecordingsByCollection(
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        musicBrainzModels: List<RecordingMusicBrainzModel>
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

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(resourceId)
            relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.RECORDING)
        }
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, RecordingForListItem> = when {
        query.isEmpty() -> {
            collectionEntityDao.getRecordingsByCollection(resourceId)
        }
        else -> {
            collectionEntityDao.getRecordingsByCollectionFiltered(
                collectionId = resourceId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: RecordingForListItem): RecordingListItemModel {
        return roomModel.toRecordingListItemModel()
    }
}
