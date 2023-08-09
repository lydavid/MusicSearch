package ly.david.data.domain.event

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.event.EventDao
import ly.david.data.room.event.toEventRoomModel

@Singleton
class EventRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val eventDao: EventDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupEvent(eventId: String): EventScaffoldModel {
        val eventRoomModel = eventDao.getEvent(eventId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(eventId)
        if (eventRoomModel != null && hasUrlsBeenSavedForEntity) {
            return eventRoomModel.toEventScaffoldModel()
        }

        val eventMusicBrainzModel = musicBrainzApiService.lookupEvent(eventId)
        eventDao.withTransaction {
            eventDao.insert(eventMusicBrainzModel.toEventRoomModel())
            relationRepository.insertAllRelations(
                entityId = eventId,
                relationMusicBrainzModels = eventMusicBrainzModel.relations,
            )
        }
        return lookupEvent(eventId)
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupEvent(
            eventId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
