package ly.david.ui.common.releasegroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ly.david.musicsearch.data.core.releasegroup.ReleaseGroupForListItem
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.ReleaseGroupMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleaseGroupsResponse
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.domain.listitem.ListItemModel
import ly.david.ui.common.paging.BrowseSortableEntityUseCase
import ly.david.ui.common.paging.SortablePagedList
import lydavidmusicsearchdatadatabase.Browse_entity_count

abstract class ReleaseGroupsByEntityViewModel(
    private val browseEntityCountDao: BrowseEntityCountDao,
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
        releaseGroupMusicBrainzModels: List<ReleaseGroupMusicBrainzModel>,
    )

    override suspend fun browseLinkedEntitiesAndStore(entityId: String, nextOffset: Int): Int {
        val response = browseReleaseGroupsByEntity(entityId, nextOffset)

        if (response.offset == 0) {
            browseEntityCountDao.insert(
                browseEntityCount = Browse_entity_count(
                    entity_id = entityId,
                    browse_entity = MusicBrainzEntity.RELEASE_GROUP,
                    local_count = response.musicBrainzModels.size,
                    remote_count = response.count
                )
            )
        } else {
            browseEntityCountDao.incrementLocalCountForEntity(
                entityId = entityId,
                browseEntity = MusicBrainzEntity.RELEASE_GROUP,
                additionalOffset = response.musicBrainzModels.size
            )
        }

        val releaseGroupMusicBrainzModels = response.musicBrainzModels
        releaseGroupDao.insertAll(releaseGroupMusicBrainzModels)
        insertAllLinkingModels(entityId, releaseGroupMusicBrainzModels)

        return releaseGroupMusicBrainzModels.size
    }

    override suspend fun getRemoteLinkedEntitiesCountByEntity(entityId: String): Int? =
        browseEntityCountDao.getBrowseEntityCount(entityId, MusicBrainzEntity.RELEASE_GROUP)?.remoteCount

    override suspend fun getLocalLinkedEntitiesCountByEntity(entityId: String): Int =
        browseEntityCountDao.getBrowseEntityCount(entityId, MusicBrainzEntity.RELEASE_GROUP)?.localCount ?: 0
}
