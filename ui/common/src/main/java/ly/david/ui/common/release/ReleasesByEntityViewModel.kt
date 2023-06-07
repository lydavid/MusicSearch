package ly.david.ui.common.release

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.domain.toReleaseListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.api.BrowseReleasesResponse
import ly.david.data.room.relation.BrowseResourceCount
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.release.ReleaseDao
import ly.david.data.room.release.ReleaseForListItem
import ly.david.data.room.release.toRoomModel
import ly.david.ui.common.paging.BrowseResourceUseCase
import ly.david.ui.common.paging.IPagedList
import ly.david.ui.common.paging.PagedList

abstract class ReleasesByEntityViewModel(
    private val relationDao: RelationDao,
    private val releaseDao: ReleaseDao,
    private val pagedList: PagedList<ReleaseForListItem, ReleaseListItemModel>
) : ViewModel(),
    IPagedList<ReleaseListItemModel> by pagedList,
    BrowseResourceUseCase<ReleaseForListItem, ReleaseListItemModel> {

    init {
        pagedList.scope = viewModelScope
        this.also { pagedList.useCase = it }
    }

    abstract suspend fun browseReleasesByEntity(entityId: String, offset: Int): BrowseReleasesResponse

    abstract suspend fun insertAllLinkingModels(
        entityId: String,
        releaseMusicBrainzModels: List<ReleaseMusicBrainzModel>
    )

    override suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = browseReleasesByEntity(resourceId, nextOffset)

        if (response.offset == 0) {
            relationDao.insertBrowseResourceCount(
                browseResourceCount = BrowseResourceCount(
                    resourceId = resourceId,
                    browseResource = MusicBrainzResource.RELEASE,
                    localCount = response.musicBrainzModels.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForResource(
                resourceId = resourceId,
                browseResource = MusicBrainzResource.RELEASE,
                additionalOffset = response.musicBrainzModels.size
            )
        }

        val releaseMusicBrainzModels = response.musicBrainzModels
        releaseDao.insertAll(releaseMusicBrainzModels.map { it.toRoomModel() })
        insertAllLinkingModels(resourceId, releaseMusicBrainzModels)

        return releaseMusicBrainzModels.size
    }

    override suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE)?.remoteCount

    override suspend fun getLocalLinkedResourcesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE)?.localCount ?: 0

    override fun transformRoomToListItemModel(roomModel: ReleaseForListItem): ReleaseListItemModel {
        return roomModel.toReleaseListItemModel()
    }
}
