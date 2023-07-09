package ly.david.data.domain.instrument

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.listitem.InstrumentListItemModel
import ly.david.data.domain.listitem.toInstrumentListItemModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.instrument.InstrumentDao
import ly.david.data.room.instrument.toInstrumentRoomModel

@Singleton
class InstrumentRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val instrumentDao: InstrumentDao,
) : RelationsListRepository {

    suspend fun lookupInstrument(instrumentId: String): InstrumentListItemModel {
        val instrumentRoomModel = instrumentDao.getInstrument(instrumentId)
        if (instrumentRoomModel != null) {
            return instrumentRoomModel.toInstrumentListItemModel()
        }

        val instrumentMusicBrainzModel = musicBrainzApiService.lookupInstrument(instrumentId)
        instrumentDao.insert(instrumentMusicBrainzModel.toInstrumentRoomModel())

        return instrumentMusicBrainzModel.toInstrumentListItemModel()
    }

    // TODO: interestingly, MB can list artists but we can't browse or lookup artist-rels for them
    //  eg. https://musicbrainz.org/instrument/1b165fa4-8510-4a3e-a2b5-2d38baf55176/artists
    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupInstrument(
            instrumentId = entityId,
            include = LookupApi.INC_ALL_RELATIONS
        ).relations
    }
}
