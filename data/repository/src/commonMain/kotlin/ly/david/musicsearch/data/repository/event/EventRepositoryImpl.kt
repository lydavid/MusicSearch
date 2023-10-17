package ly.david.musicsearch.data.repository.event

import ly.david.data.musicbrainz.EventMusicBrainzModel
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.event.EventScaffoldModel
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.domain.event.EventRepository
import ly.david.musicsearch.domain.relation.RelationRepository

class EventRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val eventDao: EventDao,
    private val relationRepository: RelationRepository,
) : EventRepository {

    override suspend fun lookupEvent(eventId: String): EventScaffoldModel {
        val event = eventDao.getEventForDetails(eventId)
        val urlRelations = relationRepository.getEntityUrlRelationships(eventId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(eventId)
        if (event != null && hasUrlsBeenSavedForEntity) {
            return event.copy(
                urls = urlRelations,
            )
        }

        val eventMusicBrainzModel = musicBrainzApi.lookupEvent(eventId)
        cache(eventMusicBrainzModel)
        return lookupEvent(eventId)
    }

    private fun cache(event: EventMusicBrainzModel) {
        eventDao.withTransaction {
            eventDao.insert(event)

            val relationWithOrderList = event.relations.toRelationWithOrderList(event.id)
            relationRepository.insertAllUrlRelations(
                entityId = event.id,
                relationWithOrderList = relationWithOrderList,
            )
        }
    }
}
