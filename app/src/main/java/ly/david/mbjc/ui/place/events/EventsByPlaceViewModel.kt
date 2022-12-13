package ly.david.mbjc.ui.place.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.EventListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.event.EventDao
import ly.david.data.persistence.event.EventPlace
import ly.david.data.persistence.event.EventPlaceDao
import ly.david.data.persistence.event.EventRoomModel
import ly.david.data.persistence.event.toEventRoomModel
import ly.david.data.persistence.relation.BrowseResourceCount
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.common.paging.BrowseResourceUseCase
import ly.david.mbjc.ui.common.paging.PagedList
import ly.david.mbjc.ui.event.EventsPagedList

@HiltViewModel
internal class EventsByPlaceViewModel @Inject constructor(
    private val eventsPagedList: EventsPagedList,
    private val musicBrainzApiService: MusicBrainzApiService,
    private val relationDao: RelationDao,
    private val eventPlaceDao: EventPlaceDao,
    private val eventDao: EventDao,
) : ViewModel(),
    PagedList<EventListItemModel> by eventsPagedList, BrowseResourceUseCase<EventRoomModel> {

    init {
        eventsPagedList.scope = viewModelScope
        eventsPagedList.useCase = this
    }

    override suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseEventsByPlace(
            placeId = resourceId,
            offset = nextOffset
        )

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
        eventPlaceDao.insertAll(
            eventMusicBrainzModels.map { event ->
                EventPlace(
                    eventId = event.id,
                    placeId = resourceId
                )
            }
        )

        return eventMusicBrainzModels.size
    }

    override suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.EVENT)?.remoteCount

    override suspend fun getLocalLinkedResourcesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.EVENT)?.localCount ?: 0

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        eventPlaceDao.deleteEventsByPlace(resourceId)
        relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.EVENT)
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, EventRoomModel> = when {
        query.isEmpty() -> {
            eventPlaceDao.getEventsByPlace(resourceId)
        }
        else -> {
            eventPlaceDao.getEventsByPlace(
                placeId = resourceId,
                query = "%$query%"
            )
        }
    }
}
