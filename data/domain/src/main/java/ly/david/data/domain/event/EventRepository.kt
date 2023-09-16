package ly.david.data.domain.event

import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.event.RoomEventDao
import ly.david.data.room.event.toEventRoomModel
import ly.david.musicsearch.data.database.dao.EventDao
import org.koin.core.annotation.Single

@Single
class EventRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val eventDao: EventDao,
    private val roomEventDao: RoomEventDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupEvent(eventId: String): EventScaffoldModel {
        val eventWithAllData = roomEventDao.getEvent(eventId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(eventId)
        if (eventWithAllData != null && hasUrlsBeenSavedForEntity) {
            return eventWithAllData.toEventScaffoldModel()
        }

        val eventMusicBrainzModel = musicBrainzApi.lookupEvent(eventId)
        roomEventDao.withTransaction {
            eventDao.insert(eventMusicBrainzModel)
            roomEventDao.insert(eventMusicBrainzModel.toEventRoomModel())
            relationRepository.insertAllUrlRelations(
                entityId = eventId,
                relationMusicBrainzModels = eventMusicBrainzModel.relations,
            )
        }
        return lookupEvent(eventId)
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupEvent(
            eventId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
