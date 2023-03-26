package ly.david.mbjc.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ly.david.data.domain.EventListItemModel
import ly.david.data.domain.toEventListItemModel
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.BrowseEventsResponse
import ly.david.data.persistence.event.EventDao
import ly.david.data.persistence.event.EventRoomModel
import ly.david.data.persistence.event.toEventRoomModel
import ly.david.data.persistence.relation.BrowseResourceCount
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.common.paging.BrowseResourceUseCase
import ly.david.mbjc.ui.common.paging.IPagedList
import ly.david.mbjc.ui.common.paging.PagedList

internal abstract class EventsByEntityViewModel(
    private val relationDao: RelationDao,
    private val eventDao: EventDao,
    private val pagedList: PagedList<EventRoomModel, EventListItemModel>,
) : ViewModel(),
    IPagedList<EventListItemModel> by pagedList,
    BrowseResourceUseCase<EventRoomModel, EventListItemModel> {

    init {
        pagedList.scope = viewModelScope
        this.also { pagedList.useCase = it }
    }

    abstract suspend fun browseEventsByEntity(entityId: String, offset: Int): BrowseEventsResponse

    abstract suspend fun insertAllLinkingModels(
        entityId: String,
        eventMusicBrainzModels: List<EventMusicBrainzModel>
    )

    override suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = browseEventsByEntity(resourceId, nextOffset)

        if (response.offset == 0) {
            relationDao.insertBrowseResourceCount(
                browseResourceCount = BrowseResourceCount(
                    resourceId = resourceId,
                    browseResource = MusicBrainzResource.EVENT,
                    localCount = response.events.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForResource(resourceId, MusicBrainzResource.EVENT, response.events.size)
        }

        val eventMusicBrainzModels = response.events
        eventDao.insertAll(eventMusicBrainzModels.map { it.toEventRoomModel() })
        insertAllLinkingModels(resourceId, eventMusicBrainzModels)

        return eventMusicBrainzModels.size
    }

    override suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.EVENT)?.remoteCount

    override suspend fun getLocalLinkedResourcesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.EVENT)?.localCount ?: 0

    override fun transformRoomToListItemModel(roomModel: EventRoomModel): EventListItemModel {
        return roomModel.toEventListItemModel()
    }
}
