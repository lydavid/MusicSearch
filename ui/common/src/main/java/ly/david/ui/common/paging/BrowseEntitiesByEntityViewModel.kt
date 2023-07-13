package ly.david.ui.common.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ly.david.data.domain.listitem.ListItemModel
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.MusicBrainzModel
import ly.david.data.network.api.Browsable
import ly.david.data.room.RoomModel
import ly.david.data.room.relation.BrowseEntityCount
import ly.david.data.room.relation.RelationDao

abstract class BrowseEntitiesByEntityViewModel<
    RM : RoomModel,
    LI : ListItemModel,
    MB : MusicBrainzModel,
    B : Browsable<MB>,
    >(
    private val byEntity: MusicBrainzEntity,
    private val relationDao: RelationDao,
    private val pagedList: PagedList<RM, LI>,
) : ViewModel(),
    IPagedList<LI> by pagedList,
    BrowseEntityUseCase<RM, LI> {

    init {
        pagedList.scope = viewModelScope
        this.also { pagedList.useCase = it }
    }

    abstract suspend fun browseEntitiesByEntity(entityId: String, offset: Int): B

    abstract suspend fun insertAllLinkingModels(
        entityId: String,
        musicBrainzModels: List<MB>,
    )

    override suspend fun browseLinkedEntitiesAndStore(entityId: String, nextOffset: Int): Int {
        val response = browseEntitiesByEntity(entityId, nextOffset)

        if (response.offset == 0) {
            relationDao.insertBrowseEntityCount(
                browseEntityCount = BrowseEntityCount(
                    entityId = entityId,
                    browseEntity = byEntity,
                    localCount = response.musicBrainzModels.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForEntity(entityId, byEntity, response.musicBrainzModels.size)
        }

        val musicBrainzModels = response.musicBrainzModels
        insertAllLinkingModels(entityId, musicBrainzModels)

        return musicBrainzModels.size
    }

    override suspend fun getRemoteLinkedEntitiesCountByEntity(entityId: String): Int? =
        relationDao.getBrowseEntityCount(entityId, byEntity)?.remoteCount

    override suspend fun getLocalLinkedEntitiesCountByEntity(entityId: String) =
        relationDao.getBrowseEntityCount(entityId, byEntity)?.localCount ?: 0
}
