package ly.david.ui.common.releasegroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ly.david.data.domain.listitem.ListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.network.api.BrowseReleaseGroupsResponse
import ly.david.data.room.relation.BrowseResourceCount
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.releasegroup.ReleaseGroupDao
import ly.david.data.room.releasegroup.ReleaseGroupForListItem
import ly.david.ui.common.paging.BrowseSortableResourceUseCase
import ly.david.ui.common.paging.SortablePagedList

abstract class ReleaseGroupsByEntityViewModel(
    private val relationDao: RelationDao,
    private val releaseGroupDao: ReleaseGroupDao,
    private val releaseGroupsPagedList: ReleaseGroupsPagedList,
) : ViewModel(),
    SortablePagedList<ListItemModel> by releaseGroupsPagedList,
    BrowseSortableResourceUseCase<ReleaseGroupForListItem> {

    init {
        releaseGroupsPagedList.scope = viewModelScope
        this.also { releaseGroupsPagedList.useCase = it }
    }

    abstract suspend fun browseReleaseGroupsByEntity(entityId: String, offset: Int): BrowseReleaseGroupsResponse

    abstract suspend fun insertAllLinkingModels(
        entityId: String,
        releaseGroupMusicBrainzModels: List<ReleaseGroupMusicBrainzModel>
    )

    override suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = browseReleaseGroupsByEntity(resourceId, nextOffset)

        if (response.offset == 0) {
            relationDao.insertBrowseResourceCount(
                browseResourceCount = BrowseResourceCount(
                    resourceId = resourceId,
                    browseResource = MusicBrainzResource.RELEASE_GROUP,
                    localCount = response.musicBrainzModels.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForResource(
                resourceId = resourceId,
                browseResource = MusicBrainzResource.RELEASE_GROUP,
                additionalOffset = response.musicBrainzModels.size
            )
        }

        val releaseGroupMusicBrainzModels = response.musicBrainzModels
        releaseGroupDao.insertAllReleaseGroupsWithArtistCredits(releaseGroupMusicBrainzModels)
        insertAllLinkingModels(resourceId, releaseGroupMusicBrainzModels)

        return releaseGroupMusicBrainzModels.size
    }

    override suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE_GROUP)?.remoteCount

    override suspend fun getLocalLinkedResourcesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE_GROUP)?.localCount ?: 0
}
