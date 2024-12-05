package ly.david.musicsearch.data.repository.event

import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.event.EventDetailsModel
import ly.david.musicsearch.shared.domain.event.EventRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class EventRepositoryImpl(
    private val eventDao: EventDao,
    private val relationRepository: RelationRepository,
    private val lookupApi: LookupApi,
) : EventRepository {

    override suspend fun lookupEvent(
        eventId: String,
        forceRefresh: Boolean,
    ): EventDetailsModel {
        if (forceRefresh) {
            delete(eventId)
        }

        val event = eventDao.getEventForDetails(eventId)
        val urlRelations = relationRepository.getRelationshipsByType(eventId)
        val visited = relationRepository.visited(eventId)
        if (event != null &&
            visited &&
            !forceRefresh
        ) {
            return event.copy(
                urls = urlRelations,
            )
        }

        val eventMusicBrainzModel = lookupApi.lookupEvent(eventId)
        cache(eventMusicBrainzModel)
        return lookupEvent(
            eventId = eventId,
            forceRefresh = false,
        )
    }

    private fun delete(id: String) {
        eventDao.withTransaction {
            eventDao.delete(id)
            relationRepository.deleteRelationshipsByType(entityId = id)
        }
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
