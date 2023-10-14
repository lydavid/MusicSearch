package ly.david.musicsearch.data.repository

import ly.david.data.musicbrainz.InstrumentMusicBrainzModel
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.core.instrument.InstrumentScaffoldModel
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.domain.instrument.InstrumentRepository
import ly.david.musicsearch.domain.relation.RelationRepository

class InstrumentRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val instrumentDao: InstrumentDao,
    private val relationRepository: RelationRepository,
) : InstrumentRepository {

    override suspend fun lookupInstrument(instrumentId: String): InstrumentScaffoldModel {
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
