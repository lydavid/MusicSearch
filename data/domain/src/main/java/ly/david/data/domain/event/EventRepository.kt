package ly.david.data.domain.event

import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.event.LegacyEventDao
import ly.david.data.room.event.toEventRoomModel
import org.koin.core.annotation.Single

@Single
class EventRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val eventDao: LegacyEventDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupEvent(eventId: String): EventScaffoldModel {
        val eventWithAllData = eventDao.getEvent(eventId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(eventId)
        if (eventWithAllData != null && hasUrlsBeenSavedForEntity) {
            return eventWithAllData.toEventScaffoldModel()
        }

        val eventMusicBrainzModel = musicBrainzApi.lookupEvent(eventId)
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
        return musicBrainzApi.lookupEvent(
            eventId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
