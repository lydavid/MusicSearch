package ly.david.data.domain.event

import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.EventDao
import lydavidmusicsearchdatadatabase.Mb_relation
import org.koin.core.annotation.Single

@Single
class EventRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val eventDao: EventDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupEvent(eventId: String): EventScaffoldModel {
        val event = eventDao.getEvent(eventId)
        val urlRelations: List<Mb_relation> = relationRepository.getEntityUrlRelationships(entityId = eventId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(eventId)
        if (event != null && hasUrlsBeenSavedForEntity) {
            return event.toEventScaffoldModel(urlRelations)
        }

        val eventMusicBrainzModel = musicBrainzApi.lookupEvent(eventId)
        eventDao.insert(eventMusicBrainzModel)
        relationRepository.insertAllUrlRelations(
            entityId = eventId,
            relationMusicBrainzModels = eventMusicBrainzModel.relations,
        )
        return lookupEvent(eventId)
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupEvent(
            eventId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
