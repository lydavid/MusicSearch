package ly.david.mbjc.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ly.david.data.domain.RecordingListItemModel
import ly.david.data.domain.toRecordingListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.network.api.BrowseRecordingsResponse
import ly.david.data.persistence.recording.RecordingDao
import ly.david.data.persistence.recording.RecordingForListItem
import ly.david.data.persistence.recording.toRoomModel
import ly.david.data.persistence.relation.BrowseResourceCount
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.common.paging.BrowseResourceUseCase
import ly.david.mbjc.ui.common.paging.IPagedList
import ly.david.mbjc.ui.common.paging.PagedList

internal abstract class RecordingsByEntityViewModel(
    private val relationDao: RelationDao,
    private val recordingDao: RecordingDao,
    private val pagedList: PagedList<RecordingForListItem, RecordingListItemModel>,
) : ViewModel(),
    IPagedList<RecordingListItemModel> by pagedList,
    BrowseResourceUseCase<RecordingForListItem, RecordingListItemModel> {

    init {
        pagedList.scope = viewModelScope
        this.also { pagedList.useCase = it }
    }

    abstract suspend fun browseRecordingsByEntity(entityId: String, offset: Int): BrowseRecordingsResponse

    abstract suspend fun insertAllLinkingModels(
        entityId: String,
        recordingMusicBrainzModels: List<RecordingMusicBrainzModel>
    )

    override suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = browseRecordingsByEntity(resourceId, nextOffset)

        if (response.offset == 0) {
            relationDao.insertBrowseResourceCount(
                browseResourceCount = BrowseResourceCount(
                    resourceId = resourceId,
                    browseResource = MusicBrainzResource.RECORDING,
                    localCount = response.recordings.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForResource(
                resourceId,
                MusicBrainzResource.RECORDING,
                response.recordings.size
            )
        }

        val recordingMusicBrainzModels = response.recordings
        recordingDao.insertAll(recordingMusicBrainzModels.map { it.toRoomModel() })
        insertAllLinkingModels(resourceId, recordingMusicBrainzModels)

        return recordingMusicBrainzModels.size
    }

    override suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RECORDING)?.remoteCount

    override suspend fun getLocalLinkedResourcesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RECORDING)?.localCount ?: 0

    override fun transformRoomToListItemModel(roomModel: RecordingForListItem): RecordingListItemModel {
        return roomModel.toRecordingListItemModel()
    }
}
