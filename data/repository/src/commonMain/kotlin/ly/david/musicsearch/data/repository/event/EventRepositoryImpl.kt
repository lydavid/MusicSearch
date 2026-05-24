package ly.david.musicsearch.data.repository.event

import app.cash.sqldelight.TransactionWithoutReturn
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.LookupEntityRepository
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.EventDetailsModel
import ly.david.musicsearch.shared.domain.event.EventRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import kotlin.time.Instant

class EventRepositoryImpl(
    private val eventDao: EventDao,
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val tagDao: TagDao,
    private val lookupApi: LookupApi,
    coroutineDispatchers: CoroutineDispatchers,
) : EventRepository, LookupEntityRepository<EventDetailsModel, EventMusicBrainzNetworkModel>(
    coroutineDispatchers = coroutineDispatchers,
) {
    override fun withTransaction(block: TransactionWithoutReturn.() -> Unit) {
        eventDao.withTransaction(block)
    }

    override suspend fun getCachedData(entityId: String): EventDetailsModel? {
        if (!relationRepository.visited(entityId)) return null
        val event = eventDao.getEventForDetails(entityId) ?: return null

        val urlRelations = relationRepository.getRelationshipsByType(entityId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.EVENT,
            mbid = entityId,
        )
        val genres = tagDao.getGenres(entityId = entityId)
        val tags = tagDao.getTags(entityId = entityId)

        return event.copy(
            urls = urlRelations,
            aliases = aliases,
            genres = genres,
            tags = tags,
        )
    }

    override suspend fun getRemoteData(entityId: String): EventMusicBrainzNetworkModel {
        return lookupApi.lookupEvent(eventId = entityId)
    }

    override fun delete(entityId: String) {
        eventDao.delete(entityId)
        relationRepository.deleteRelationshipsByType(entityId = entityId)
        tagDao.deleteByEntity(entityId = entityId)
    }

    override fun cache(
        oldId: String,
        musicBrainzNetworkModel: EventMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        eventDao.upsert(
            oldId = oldId,
            event = musicBrainzNetworkModel,
        )

        aliasDao.insertAll(listOf(musicBrainzNetworkModel))

        val relationWithOrderList =
            musicBrainzNetworkModel.relations.toRelationWithOrderList(musicBrainzNetworkModel.id)
        relationRepository.insertRelations(
            entityId = musicBrainzNetworkModel.id,
            relationWithOrderList = relationWithOrderList,
            lastUpdated = lastUpdated,
        )

        tagDao.insertAll(
            entityId = musicBrainzNetworkModel.id,
            genres = musicBrainzNetworkModel.genres,
            tags = musicBrainzNetworkModel.tags,
        )
    }
}
