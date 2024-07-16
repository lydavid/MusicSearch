package ly.david.musicsearch.data.repository.instrument

import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.instrument.InstrumentDetailsModel
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.instrument.InstrumentRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class InstrumentRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val instrumentDao: InstrumentDao,
    private val relationRepository: RelationRepository,
) : InstrumentRepository {

    override suspend fun lookupInstrument(instrumentId: String): InstrumentDetailsModel {
        val instrument = instrumentDao.getInstrumentForDetails(instrumentId)
        val urlRelations = relationRepository.getEntityUrlRelationships(instrumentId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(instrumentId)
        if (instrument != null && hasUrlsBeenSavedForEntity) {
            return instrument.copy(urls = urlRelations)
        }

        val instrumentMusicBrainzModel = musicBrainzApi.lookupInstrument(instrumentId)
        cache(instrumentMusicBrainzModel)
        return lookupInstrument(instrumentId)
    }

    private fun cache(instrument: InstrumentMusicBrainzModel) {
        instrumentDao.withTransaction {
            instrumentDao.insert(instrument)

            val relationWithOrderList = instrument.relations.toRelationWithOrderList(instrument.id)
            relationRepository.insertAllUrlRelations(
                entityId = instrument.id,
                relationWithOrderList = relationWithOrderList,
            )
        }
    }
}
