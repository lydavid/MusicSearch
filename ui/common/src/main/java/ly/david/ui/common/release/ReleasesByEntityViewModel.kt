package ly.david.ui.common.release

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.listitem.ReleaseListItemModel
import ly.david.data.domain.listitem.toReleaseListItemModel
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.data.room.relation.BrowseEntityCount
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.release.ReleaseDao
import ly.david.data.room.release.ReleaseForListItem
import ly.david.data.room.release.toRoomModel
import ly.david.ui.common.paging.BrowseEntityUseCase
import ly.david.ui.common.paging.IPagedList

abstract class ReleasesByEntityViewModel(
    private val relationDao: RelationDao,
    private val releaseDao: ReleaseDao,
    private val pagedList: ReleasesPagedList,
) : ViewModel(),
    IPagedList<ReleaseListItemModel> by pagedList,
    BrowseEntityUseCase<ReleaseForListItem, ReleaseListItemModel> {

    init {
        pagedList.scope = viewModelScope
        this.also { pagedList.useCase = it }
    }

    abstract suspend fun browseReleasesByEntity(entityId: String, offset: Int): BrowseReleasesResponse

    abstract suspend fun insertAllLinkingModels(
        entityId: String,
        releaseMusicBrainzModels: List<ReleaseMusicBrainzModel>,
    )

    override suspend fun browseLinkedEntitiesAndStore(entityId: String, nextOffset: Int): Int {
        val response = browseReleasesByEntity(entityId, nextOffset)

        if (response.offset == 0) {
            relationDao.insertBrowseEntityCount(
                browseEntityCount = BrowseEntityCount(
                    entityId = entityId,
                    browseEntity = MusicBrainzEntity.RELEASE,
                    localCount = response.musicBrainzModels.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForEntity(
                entityId = entityId,
                browseEntity = MusicBrainzEntity.RELEASE,
                additionalOffset = response.musicBrainzModels.size
            )
        }

        val releaseMusicBrainzModels = response.musicBrainzModels
        releaseDao.insertAll(releaseMusicBrainzModels.map { it.toRoomModel() })
        insertAllLinkingModels(entityId, releaseMusicBrainzModels)

        return releaseMusicBrainzModels.size
    }

    override suspend fun getRemoteLinkedEntitiesCountByEntity(entityId: String): Int? =
        relationDao.getBrowseEntityCount(entityId, MusicBrainzEntity.RELEASE)?.remoteCount

    override suspend fun getLocalLinkedEntitiesCountByEntity(entityId: String) =
        relationDao.getBrowseEntityCount(entityId, MusicBrainzEntity.RELEASE)?.localCount ?: 0

    override fun transformRoomToListItemModel(roomModel: ReleaseForListItem): ReleaseListItemModel {
        return roomModel.toReleaseListItemModel()
    }
}
