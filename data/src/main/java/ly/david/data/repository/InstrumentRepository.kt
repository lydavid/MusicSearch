package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.Instrument
import ly.david.data.domain.InstrumentUiModel
import ly.david.data.domain.toInstrumentUiModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.history.LookupHistory
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.persistence.instrument.InstrumentDao
import ly.david.data.persistence.instrument.toInstrumentRoomModel
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationRoomModel
import ly.david.data.persistence.relation.toRelationRoomModel

@Singleton
class InstrumentRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val instrumentDao: InstrumentDao,
    private val relationDao: RelationDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var instrument: InstrumentUiModel? = null

    suspend fun lookupInstrument(instrumentId: String): InstrumentUiModel =
        instrument ?: run {

            // Use cached model.
            val instrumentRoomModel = instrumentDao.getInstrument(instrumentId)
            if (instrumentRoomModel != null) {
                incrementOrInsertLookupHistory(instrumentRoomModel)
                return instrumentRoomModel.toInstrumentUiModel()
            }

            val instrumentMusicBrainzModel = musicBrainzApiService.lookupInstrument(instrumentId)
            instrumentDao.insert(instrumentMusicBrainzModel.toInstrumentRoomModel())

            val relations = mutableListOf<RelationRoomModel>()
            instrumentMusicBrainzModel.relations?.forEachIndexed { index, relationMusicBrainzModel ->
                relationMusicBrainzModel.toRelationRoomModel(
                    resourceId = instrumentId,
                    order = index
                )?.let { relationRoomModel ->
                    relations.add(relationRoomModel)
                }
            }
            relationDao.insertAll(relations)

            incrementOrInsertLookupHistory(instrumentMusicBrainzModel)
            instrumentMusicBrainzModel.toInstrumentUiModel()
        }

    private suspend fun incrementOrInsertLookupHistory(instrument: Instrument) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                title = instrument.name,
                resource = MusicBrainzResource.INSTRUMENT,
                mbid = instrument.id
            )
        )
    }
}
