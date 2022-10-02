package ly.david.mbjc.ui.instrument

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Instrument
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.history.LookupHistory
import ly.david.mbjc.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.data.persistence.instrument.InstrumentDao
import ly.david.mbjc.data.persistence.instrument.toInstrumentRoomModel
import ly.david.mbjc.data.persistence.relation.RelationRoomModel
import ly.david.mbjc.data.persistence.relation.toRelationRoomModel

@Singleton
internal class InstrumentRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val instrumentDao: InstrumentDao,
    private val relationDao: RelationDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var instrument: Instrument? = null

    suspend fun lookupInstrument(instrumentId: String): Instrument =
        instrument ?: run {

            // Use cached model.
            val instrumentRoomModel = instrumentDao.getInstrument(instrumentId)
            if (instrumentRoomModel != null) {
                incrementOrInsertLookupHistory(instrumentRoomModel)
                return instrumentRoomModel
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
            instrumentMusicBrainzModel
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
