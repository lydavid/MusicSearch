package ly.david.ui.common.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ly.david.data.domain.ListItemModel
import ly.david.data.network.MusicBrainzModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.Browsable
import ly.david.data.persistence.RoomModel
import ly.david.data.persistence.relation.BrowseResourceCount
import ly.david.data.persistence.relation.RelationDao

abstract class BrowseEntitiesByEntityViewModel
<RM : RoomModel, LI : ListItemModel, MB : MusicBrainzModel, B : Browsable<MB>>(
    private val byEntity: MusicBrainzResource,
    private val relationDao: RelationDao,
    private val pagedList: PagedList<RM, LI>,
) : ViewModel(),
    IPagedList<LI> by pagedList,
    BrowseResourceUseCase<RM, LI> {

    init {
        pagedList.scope = viewModelScope
        this.also { pagedList.useCase = it }
    }

    abstract suspend fun browseEntitiesByEntity(entityId: String, offset: Int): B

    abstract suspend fun insertAllLinkingModels(
        entityId: String,
        musicBrainzModels: List<MB>
    )

    override suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = browseEntitiesByEntity(resourceId, nextOffset)

        if (response.offset == 0) {
            relationDao.insertBrowseResourceCount(
                browseResourceCount = BrowseResourceCount(
                    resourceId = resourceId,
                    browseResource = byEntity,
                    localCount = response.musicBrainzModels.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForResource(resourceId, byEntity, response.musicBrainzModels.size)
        }

        val musicBrainzModels = response.musicBrainzModels
        insertAllLinkingModels(resourceId, musicBrainzModels)

        return musicBrainzModels.size
    }

    override suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, byEntity)?.remoteCount

    override suspend fun getLocalLinkedResourcesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceCount(resourceId, byEntity)?.localCount ?: 0
}
