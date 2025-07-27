package ly.david.musicsearch.data.repository.instrument

import kotlinx.datetime.Instant
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.details.InstrumentDetailsModel
import ly.david.musicsearch.shared.domain.instrument.InstrumentRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository

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

        val instrument = instrumentDao.getInstrumentForDetails(instrumentId)
        val urlRelations = relationRepository.getRelationshipsByType(instrumentId)
        val visited = relationRepository.visited(instrumentId)
        if (instrument != null && visited) {
            return instrument.copy(urls = urlRelations)
        }

        val instrumentMusicBrainzModel = lookupApi.lookupInstrument(instrumentId)
        cache(
            instrument = instrumentMusicBrainzModel,
            lastUpdated = lastUpdated,
        )
        return lookupInstrument(
            instrumentId = instrumentId,
            forceRefresh = false,
            lastUpdated = lastUpdated,
        )
    }

    private fun delete(id: String) {
        instrumentDao.withTransaction {
            instrumentDao.delete(id)
            relationRepository.deleteRelationshipsByType(id)
        }
    }

    private fun cache(
        instrument: InstrumentMusicBrainzNetworkModel,
        lastUpdated: Instant,
    ) {
        instrumentDao.withTransaction {
            instrumentDao.insert(instrument)

            aliasDao.insertReplaceAll(listOf(instrument))

            val relationWithOrderList = instrument.relations.toRelationWithOrderList(instrument.id)
            relationRepository.insertAllUrlRelations(
                entityId = instrument.id,
                relationWithOrderList = relationWithOrderList,
                lastUpdated = lastUpdated,
            )
        }
    }
}
