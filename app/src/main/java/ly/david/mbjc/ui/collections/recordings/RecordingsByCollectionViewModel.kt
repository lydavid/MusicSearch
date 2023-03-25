package ly.david.mbjc.ui.collections.recordings

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.RecordingListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.network.api.BrowseRecordingsResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.collection.CollectionEntityDao
import ly.david.data.persistence.collection.CollectionEntityRoomModel
import ly.david.data.persistence.recording.RecordingDao
import ly.david.data.persistence.recording.RecordingForListItem
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.common.RecordingsByEntityViewModel
import ly.david.mbjc.ui.common.paging.PagedList

@HiltViewModel
internal class RecordingsByCollectionViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val collectionEntityDao: CollectionEntityDao,
    private val relationDao: RelationDao,
    recordingDao: RecordingDao,
    pagedList: PagedList<RecordingForListItem, RecordingListItemModel>,
) : RecordingsByEntityViewModel(
    relationDao = relationDao,
    recordingDao = recordingDao,
    pagedList = pagedList
) {

    override suspend fun browseRecordingsByEntity(entityId: String, offset: Int): BrowseRecordingsResponse {
        return musicBrainzApiService.browseRecordingsByCollection(
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        recordingMusicBrainzModels: List<RecordingMusicBrainzModel>
    ) {
        collectionEntityDao.insertAll(
            recordingMusicBrainzModels.map { recording ->
                CollectionEntityRoomModel(
                    id = entityId,
                    entityId = recording.id
                )
            }
        )
    }

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteCollectionEntityLinks(resourceId)
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
}
