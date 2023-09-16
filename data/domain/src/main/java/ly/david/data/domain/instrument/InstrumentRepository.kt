package ly.david.data.domain.instrument

import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.instrument.InstrumentDao
import ly.david.data.room.instrument.toInstrumentRoomModel
import org.koin.core.annotation.Single

@Single
class InstrumentRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val instrumentDao: InstrumentDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupInstrument(instrumentId: String): InstrumentScaffoldModel {
        val instrumentWithAllData = instrumentDao.getInstrument(instrumentId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(instrumentId)
        if (instrumentWithAllData != null && hasUrlsBeenSavedForEntity) {
            return instrumentWithAllData.toInstrumentListItemModel()
        }

        val instrumentMusicBrainzModel = musicBrainzApi.lookupInstrument(instrumentId)
        instrumentDao.withTransaction {
            instrumentDao.insert(instrumentMusicBrainzModel.toInstrumentRoomModel())
            relationRepository.insertAllUrlRelations(
                entityId = instrumentId,
                relationMusicBrainzModels = instrumentMusicBrainzModel.relations,
            )
        }
        return lookupInstrument(instrumentId)
    }

    // TODO: interestingly, MB can list artists but we can't browse or lookup artist-rels for them
    //  eg. https://musicbrainz.org/instrument/1b165fa4-8510-4a3e-a2b5-2d38baf55176/artists
    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupInstrument(
            instrumentId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
