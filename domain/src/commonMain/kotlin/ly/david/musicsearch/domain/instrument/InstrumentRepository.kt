package ly.david.musicsearch.domain.instrument

import ly.david.data.musicbrainz.InstrumentMusicBrainzModel
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.domain.RelationsListRepository
import ly.david.musicsearch.domain.relation.RelationRepository
import org.koin.core.annotation.Single

@Single
class InstrumentRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val instrumentDao: InstrumentDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupInstrument(instrumentId: String): InstrumentScaffoldModel {
        val instrument = instrumentDao.getInstrument(instrumentId)
        val urlRelations = relationRepository.getEntityUrlRelationships(instrumentId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(instrumentId)
        if (instrument != null && hasUrlsBeenSavedForEntity) {
            return instrument.toInstrumentListItemModel(urlRelations)
        }

        val instrumentMusicBrainzModel = musicBrainzApi.lookupInstrument(instrumentId)
        cache(instrumentMusicBrainzModel)
        return lookupInstrument(instrumentId)
    }

    private fun cache(instrument: InstrumentMusicBrainzModel) {
        instrumentDao.withTransaction {
            instrumentDao.insert(instrument)
            relationRepository.insertAllUrlRelations(
                entityId = instrument.id,
                relationMusicBrainzModels = instrument.relations,
            )
        }
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
