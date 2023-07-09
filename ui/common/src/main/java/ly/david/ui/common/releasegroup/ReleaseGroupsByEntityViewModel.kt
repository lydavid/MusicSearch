package ly.david.ui.common.releasegroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ly.david.data.domain.listitem.ListItemModel
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.network.api.BrowseReleaseGroupsResponse
import ly.david.data.room.relation.BrowseEntityCount
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.releasegroup.ReleaseGroupDao
import ly.david.data.room.releasegroup.ReleaseGroupForListItem
import ly.david.ui.common.paging.BrowseSortableEntityUseCase
import ly.david.ui.common.paging.SortablePagedList

abstract class ReleaseGroupsByEntityViewModel(
    private val relationDao: RelationDao,
    private val releaseGroupDao: ReleaseGroupDao,
    private val releaseGroupsPagedList: ReleaseGroupsPagedList,
) : ViewModel(),
    SortablePagedList<ListItemModel> by releaseGroupsPagedList,
    BrowseSortableEntityUseCase<ReleaseGroupForListItem> {

    init {
        releaseGroupsPagedList.scope = viewModelScope
        this.also { releaseGroupsPagedList.useCase = it }
    }

    abstract suspend fun browseReleaseGroupsByEntity(entityId: String, offset: Int): BrowseReleaseGroupsResponse

    abstract suspend fun insertAllLinkingModels(
        entityId: String,
        releaseGroupMusicBrainzModels: List<ReleaseGroupMusicBrainzModel>
    )

    override suspend fun browseLinkedEntitiesAndStore(entityId: String, nextOffset: Int): Int {
        val response = browseReleaseGroupsByEntity(entityId, nextOffset)

        if (response.offset == 0) {
            relationDao.insertBrowseEntityCount(
                browseEntityCount = BrowseEntityCount(
                    entityId = entityId,
                    browseEntity = MusicBrainzEntity.RELEASE_GROUP,
                    localCount = response.musicBrainzModels.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForEntity(
                entityId = entityId,
                browseEntity = MusicBrainzEntity.RELEASE_GROUP,
                additionalOffset = response.musicBrainzModels.size
            )
        }

        val releaseGroupMusicBrainzModels = response.musicBrainzModels
        releaseGroupDao.insertAllReleaseGroupsWithArtistCredits(releaseGroupMusicBrainzModels)
        insertAllLinkingModels(entityId, releaseGroupMusicBrainzModels)

        return releaseGroupMusicBrainzModels.size
    }

    override suspend fun getRemoteLinkedEntitiesCountByEntity(entityId: String): Int? =
        relationDao.getBrowseEntityCount(entityId, MusicBrainzEntity.RELEASE_GROUP)?.remoteCount

    override suspend fun getLocalLinkedEntitiesCountByEntity(entityId: String) =
        relationDao.getBrowseEntityCount(entityId, MusicBrainzEntity.RELEASE_GROUP)?.localCount ?: 0
}
