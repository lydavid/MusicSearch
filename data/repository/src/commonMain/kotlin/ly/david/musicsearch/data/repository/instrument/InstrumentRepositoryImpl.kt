package ly.david.musicsearch.data.repository.instrument

import app.cash.sqldelight.TransactionWithoutReturn
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.LookupEntityRepository
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.InstrumentDetailsModel
import ly.david.musicsearch.shared.domain.instrument.InstrumentRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import kotlin.time.Instant

class InstrumentRepositoryImpl(
    private val instrumentDao: InstrumentDao,
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val tagDao: TagDao,
    private val lookupApi: LookupApi,
    coroutineDispatchers: CoroutineDispatchers,
) : InstrumentRepository, LookupEntityRepository<InstrumentDetailsModel, InstrumentMusicBrainzNetworkModel>(
    relationRepository = relationRepository,
    aliasDao = aliasDao,
    tagDao = tagDao,
    coroutineDispatchers = coroutineDispatchers,
) {
    override fun withTransaction(block: TransactionWithoutReturn.() -> Unit) {
        instrumentDao.withTransaction(block)
    }

    override suspend fun getCachedData(entityId: String): InstrumentDetailsModel? {
        if (!relationRepository.visited(entityId)) return null
        val instrument = instrumentDao.getInstrumentForDetails(entityId) ?: return null

        val urlRelations = relationRepository.getRelationshipsByType(entityId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.INSTRUMENT,
            mbid = entityId,
        )
        val genres = tagDao.getGenres(entityId = entityId)
        val tags = tagDao.getTags(entityId = entityId)

        return instrument.copy(
            urls = urlRelations,
            aliases = aliases,
            genres = genres,
            tags = tags,
        )
    }

    override suspend fun getRemoteData(entityId: String): InstrumentMusicBrainzNetworkModel {
        return lookupApi.lookupInstrument(instrumentId = entityId)
    }

    override fun delete(entityId: String) {
        instrumentDao.delete(entityId)
        relationRepository.deleteRelationshipsByType(entityId)
        tagDao.deleteByEntity(entityId = entityId)
    }

    override fun cache(
        oldId: String,
        musicBrainzNetworkModel: InstrumentMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        instrumentDao.upsert(
            oldId = oldId,
            instrument = musicBrainzNetworkModel,
        )

        super.cache(oldId, musicBrainzNetworkModel, lastUpdated)
    }
}
