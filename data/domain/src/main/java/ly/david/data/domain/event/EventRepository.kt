package ly.david.data.domain.event

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.listitem.EventListItemModel
import ly.david.data.domain.listitem.toEventListItemModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.event.EventDao
import ly.david.data.room.event.toEventRoomModel

@Singleton
class EventRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val eventDao: EventDao,
) : RelationsListRepository {

    suspend fun lookupEvent(eventId: String): EventListItemModel {
        val eventRoomModel = eventDao.getEvent(eventId)
        if (eventRoomModel != null) {
            return eventRoomModel.toEventListItemModel()
        }

        val eventMusicBrainzModel = musicBrainzApiService.lookupEvent(eventId)
        eventDao.insert(eventMusicBrainzModel.toEventRoomModel())

        return eventMusicBrainzModel.toEventListItemModel()
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupEvent(
            eventId = entityId,
            include = LookupApi.INC_ALL_RELATIONS
        ).relations
    }
}
