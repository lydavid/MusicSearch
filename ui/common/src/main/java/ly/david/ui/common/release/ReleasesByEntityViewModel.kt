package ly.david.ui.common.release

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ly.david.data.core.release.ReleaseForListItem
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.listitem.ReleaseListItemModel
import ly.david.data.domain.listitem.toReleaseListItemModel
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.ui.common.paging.BrowseEntityUseCase
import ly.david.ui.common.paging.IPagedList
import lydavidmusicsearchdatadatabase.Browse_entity_count

abstract class ReleasesByEntityViewModel(
    private val browseEntityCountDao: BrowseEntityCountDao,
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
            browseEntityCountDao.insert(
                browseEntityCount = Browse_entity_count(
                    entity_id = entityId,
                    browse_entity = MusicBrainzEntity.RELEASE,
                    local_count = response.musicBrainzModels.size,
                    remote_count = response.count
                )
            )
        } else {
            browseEntityCountDao.incrementLocalCountForEntity(
                entityId = entityId,
                browseEntity = MusicBrainzEntity.RELEASE,
                additionalOffset = response.musicBrainzModels.size
            )
        }

        val releaseMusicBrainzModels = response.musicBrainzModels
        releaseDao.insertAll(releaseMusicBrainzModels)
        insertAllLinkingModels(entityId, releaseMusicBrainzModels)

        return releaseMusicBrainzModels.size
    }

    override suspend fun getRemoteLinkedEntitiesCountByEntity(entityId: String): Int? =
        browseEntityCountDao.getBrowseEntityCount(entityId, MusicBrainzEntity.RELEASE)?.remoteCount

    override suspend fun getLocalLinkedEntitiesCountByEntity(entityId: String) =
        browseEntityCountDao.getBrowseEntityCount(entityId, MusicBrainzEntity.RELEASE)?.localCount ?: 0

    override fun transformDatabaseToListItemModel(databaseModel: ReleaseForListItem): ReleaseListItemModel {
        return databaseModel.toReleaseListItemModel()
    }
}
