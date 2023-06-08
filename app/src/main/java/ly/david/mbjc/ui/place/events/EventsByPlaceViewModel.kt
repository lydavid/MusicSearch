package ly.david.mbjc.ui.place.events

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.listitem.EventListItemModel
import ly.david.data.domain.listitem.toEventListItemModel
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.BrowseEventsResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.event.EventDao
import ly.david.data.room.place.events.EventPlace
import ly.david.data.room.place.events.EventPlaceDao
import ly.david.data.room.event.EventRoomModel
import ly.david.data.room.event.toEventRoomModel
import ly.david.data.room.relation.RelationDao
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.paging.PagedList

@HiltViewModel
internal class EventsByPlaceViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val eventPlaceDao: EventPlaceDao,
    private val eventDao: EventDao,
    private val relationDao: RelationDao,
    pagedList: PagedList<EventRoomModel, EventListItemModel>,
) : BrowseEntitiesByEntityViewModel<EventRoomModel, EventListItemModel, EventMusicBrainzModel, BrowseEventsResponse>(
    byEntity = MusicBrainzResource.EVENT,
    relationDao = relationDao,
    pagedList = pagedList
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseEventsResponse {
        return musicBrainzApiService.browseEventsByPlace(
            placeId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<EventMusicBrainzModel>) {
        eventDao.insertAll(musicBrainzModels.map { it.toEventRoomModel() })
        eventPlaceDao.insertAll(
            musicBrainzModels.map { event ->
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

    override fun transformRoomToListItemModel(roomModel: EventRoomModel): EventListItemModel {
        return roomModel.toEventListItemModel()
    }
}
