package ly.david.musicsearch.data.repository.instrument

import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.details.InstrumentDetailsModel
import ly.david.musicsearch.shared.domain.instrument.InstrumentRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import kotlin.time.Instant

class InstrumentRepositoryImpl(
    private val instrumentDao: InstrumentDao,
    private val relationRepository: RelationRepository,
    private val aliasDao: AliasDao,
    private val lookupApi: LookupApi,
) : InstrumentRepository {

    override suspend fun lookupInstrument(
        instrumentId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): InstrumentDetailsModel {
        if (forceRefresh) {
            delete(instrumentId)
        }

        val cachedData = getCachedData(instrumentId)
        return if (cachedData != null && !forceRefresh) {
            cachedData
        } else {
            val instrumentMusicBrainzModel = lookupApi.lookupInstrument(instrumentId)
            cache(
                oldId = instrumentId,
                instrument = instrumentMusicBrainzModel,
                lastUpdated = lastUpdated,
            )
            getCachedData(instrumentMusicBrainzModel.id) ?: error("Failed to get cached data")
        }
    }

    private fun getCachedData(instrumentId: String): InstrumentDetailsModel? {
        if (!relationRepository.visited(instrumentId)) return null
        val instrument = instrumentDao.getInstrumentForDetails(instrumentId) ?: return null

        val urlRelations = relationRepository.getRelationshipsByType(instrumentId)
        val aliases = aliasDao.getAliases(
            entityType = MusicBrainzEntityType.INSTRUMENT,
            mbid = instrumentId,
        )

        return instrument.copy(
            urls = urlRelations,
            aliases = aliases,
        )
    }

    private fun delete(id: String) {
        instrumentDao.withTransaction {
            instrumentDao.delete(id)
            relationRepository.deleteRelationshipsByType(id)
        }
    }

    private fun cache(
        oldId: String,
        instrument: InstrumentMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        instrumentDao.withTransaction {
            instrumentDao.upsert(
                oldId = oldId,
                instrument = instrument,
            )

            aliasDao.insertAll(listOf(instrument))

            val relationWithOrderList = instrument.relations.toRelationWithOrderList(instrument.id)
            relationRepository.insertAllUrlRelations(
                entityId = instrument.id,
                relationWithOrderList = relationWithOrderList,
                lastUpdated = lastUpdated,
            )
        }
    }
}
