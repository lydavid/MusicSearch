package ly.david.mbjc.ui.place.events

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.EventListItemModel
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.BrowseEventsResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.event.EventDao
import ly.david.data.persistence.event.EventPlace
import ly.david.data.persistence.event.EventPlaceDao
import ly.david.data.persistence.event.EventRoomModel
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.common.EventsByEntityViewModel
import ly.david.mbjc.ui.common.paging.PagedList

@HiltViewModel
internal class EventsByPlaceViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val eventPlaceDao: EventPlaceDao,
    private val relationDao: RelationDao,
    eventDao: EventDao,
    pagedList: PagedList<EventRoomModel, EventListItemModel>,
) : EventsByEntityViewModel(
    relationDao = relationDao,
    eventDao = eventDao,
    pagedList = pagedList
) {

    override suspend fun browseEventsByEntity(entityId: String, offset: Int): BrowseEventsResponse {
        return musicBrainzApiService.browseEventsByPlace(
            placeId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, eventMusicBrainzModels: List<EventMusicBrainzModel>) {
        eventPlaceDao.insertAll(
            eventMusicBrainzModels.map { event ->
                EventPlace(
                    eventId = event.id,
                    placeId = entityId
                )
            }
        )
    }

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
            eventPlaceDao.getEventsByPlaceFiltered(
                placeId = resourceId,
                query = "%$query%"
            )
        }
    }
}
