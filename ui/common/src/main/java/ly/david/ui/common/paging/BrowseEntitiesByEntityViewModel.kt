package ly.david.ui.common.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.listitem.ListItemModel
import ly.david.data.musicbrainz.MusicBrainzModel
import ly.david.data.musicbrainz.api.Browsable
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import lydavidmusicsearchdatadatabase.Browse_entity_count

abstract class BrowseEntitiesByEntityViewModel<
    RM : Any,
    LI : ListItemModel,
    MB : MusicBrainzModel,
    B : Browsable<MB>,
    >(
    private val byEntity: MusicBrainzEntity,
    private val browseEntityCountDao: BrowseEntityCountDao,
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
            browseEntityCountDao.insert(
                browseEntityCount = Browse_entity_count(
                    entity_id = entityId,
                    browse_entity = byEntity,
                    local_count = response.musicBrainzModels.size,
                    remote_count = response.count
                )
            )
        } else {
            browseEntityCountDao.incrementLocalCountForEntity(entityId, byEntity, response.musicBrainzModels.size)
        }

        val musicBrainzModels = response.musicBrainzModels
        insertAllLinkingModels(entityId, musicBrainzModels)

        return musicBrainzModels.size
    }

    override suspend fun getRemoteLinkedEntitiesCountByEntity(entityId: String): Int? =
        browseEntityCountDao.getBrowseEntityCount(entityId, byEntity)?.remote_count

    override suspend fun getLocalLinkedEntitiesCountByEntity(entityId: String) =
        browseEntityCountDao.getBrowseEntityCount(entityId, byEntity)?.local_count ?: 0
}
