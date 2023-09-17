package ly.david.mbjc.ui.place.events

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.listitem.EventListItemModel
import ly.david.data.domain.listitem.toEventListItemModel
import ly.david.data.musicbrainz.EventMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseEventsResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.event.RoomEventDao
import ly.david.data.room.event.EventRoomModel
import ly.david.data.room.event.toEventRoomModel
import ly.david.data.room.place.events.RoomEventPlace
import ly.david.data.room.place.events.RoomEventPlaceDao
import ly.david.data.room.relation.RoomRelationDao
import ly.david.ui.common.event.EventsPagedList
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class EventsByPlaceViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val eventPlaceDao: RoomEventPlaceDao,
    private val eventDao: RoomEventDao,
    private val relationDao: RoomRelationDao,
    pagedList: EventsPagedList,
) : BrowseEntitiesByEntityViewModel<EventRoomModel, EventListItemModel, EventMusicBrainzModel, BrowseEventsResponse>(
    byEntity = MusicBrainzEntity.EVENT,
    relationDao = relationDao,
    pagedList = pagedList
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseEventsResponse {
        return musicBrainzApi.browseEventsByPlace(
            placeId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<EventMusicBrainzModel>) {
        eventDao.insertAll(musicBrainzModels.map { it.toEventRoomModel() })
        eventPlaceDao.insertAll(
            musicBrainzModels.map { event ->
                RoomEventPlace(
                    eventId = event.id,
                    placeId = entityId
                )
            }
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        eventPlaceDao.deleteEventsByPlace(entityId)
        relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.EVENT)
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, EventRoomModel> = when {
        query.isEmpty() -> {
            eventPlaceDao.getEventsByPlace(entityId)
        }
        else -> {
            eventPlaceDao.getEventsByPlaceFiltered(
                placeId = entityId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: EventRoomModel): EventListItemModel {
        return roomModel.toEventListItemModel()
    }
}
