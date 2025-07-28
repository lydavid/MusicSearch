package ly.david.musicsearch.data.repository.event

import kotlinx.datetime.Instant
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.details.EventDetailsModel
import ly.david.musicsearch.shared.domain.event.EventRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class EventRepositoryImpl(
    private val eventDao: EventDao,
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val lookupApi: LookupApi,
) : EventRepository {

    override suspend fun lookupEvent(
        eventId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): EventDetailsModel {
        if (forceRefresh) {
            delete(eventId)
        }

        val event = eventDao.getEventForDetails(eventId)
        val urlRelations = relationRepository.getRelationshipsByType(eventId)
        val visited = relationRepository.visited(eventId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntity.EVENT,
            mbid = eventId,
        )

        if (event != null &&
            visited &&
            !forceRefresh
        ) {
            return event.copy(
                urls = urlRelations,
                aliases = aliases,
            )
        }

        val eventMusicBrainzModel = lookupApi.lookupEvent(eventId)
        cache(
            event = eventMusicBrainzModel,
            lastUpdated = lastUpdated,
        )
        return lookupEvent(
            eventId = eventId,
            forceRefresh = false,
            lastUpdated = lastUpdated,
        )
    }

    private fun delete(id: String) {
        eventDao.withTransaction {
            eventDao.delete(id)
            relationRepository.deleteRelationshipsByType(entityId = id)
        }
    }

    private fun cache(
        event: EventMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        eventDao.withTransaction {
            eventDao.insert(event)

            aliasDao.insertReplaceAll(listOf(event))

            val relationWithOrderList = event.relations.toRelationWithOrderList(event.id)
            relationRepository.insertAllUrlRelations(
                entityId = event.id,
                relationWithOrderList = relationWithOrderList,
                lastUpdated = lastUpdated,
            )
        }
    }
}
