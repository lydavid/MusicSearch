package ly.david.musicsearch.data.repository.event

import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.details.EventDetailsModel
import ly.david.musicsearch.shared.domain.event.EventRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import kotlin.time.Instant

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

        val cachedData = getCachedData(eventId)
        return if (cachedData != null && !forceRefresh) {
            cachedData
        } else {
            val eventMusicBrainzModel = lookupApi.lookupEvent(eventId)
            cache(
                oldId = eventId,
                event = eventMusicBrainzModel,
                lastUpdated = lastUpdated,
            )
            getCachedData(eventMusicBrainzModel.id) ?: error("Failed to get cached data")
        }
    }

    private fun getCachedData(eventId: String): EventDetailsModel? {
        if (!relationRepository.visited(eventId)) return null
        val event = eventDao.getEventForDetails(eventId) ?: return null

        val urlRelations = relationRepository.getRelationshipsByType(eventId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.EVENT,
            mbid = eventId,
        )

        return event.copy(
            urls = urlRelations,
            aliases = aliases,
        )
    }

    private fun delete(id: String) {
        eventDao.withTransaction {
            eventDao.delete(id)
            relationRepository.deleteRelationshipsByType(entityId = id)
        }
    }

    private fun cache(
        oldId: String,
        event: EventMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        eventDao.withTransaction {
            eventDao.upsert(
                oldId = oldId,
                event = event,
            )

            aliasDao.insertAll(listOf(event))

            val relationWithOrderList = event.relations.toRelationWithOrderList(event.id)
            relationRepository.insertAllUrlRelations(
                entityId = event.id,
                relationWithOrderList = relationWithOrderList,
                lastUpdated = lastUpdated,
            )
        }
    }
}
