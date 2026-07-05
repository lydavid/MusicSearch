package ly.david.musicsearch.data.repository.helpers

import ly.david.data.test.preferences.NoOpMusicBrainzAuthStore
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.repository.instrument.InstrumentRepositoryImpl
import ly.david.musicsearch.data.repository.relation.RelationRepositoryImpl
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.instrument.InstrumentRepository

interface TestInstrumentRepository {

    val lookupApi: LookupApi
    val relationsMetadataDao: RelationsMetadataDao
    val detailsMetadataDao: DetailsMetadataDao
    val relationDao: RelationDao
    val instrumentDao: InstrumentDao
    val aliasDao: AliasDao
    val tagDao: TagDao
    val coroutineDispatchers: CoroutineDispatchers

    fun createInstrumentRepository(
        musicBrainzAuthStore: NoOpMusicBrainzAuthStore = NoOpMusicBrainzAuthStore(),
    ): InstrumentRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = lookupApi,
            relationsMetadataDao = relationsMetadataDao,
            detailsMetadataDao = detailsMetadataDao,
            relationDao = relationDao,
        )
        return InstrumentRepositoryImpl(
            instrumentDao = instrumentDao,
            relationRepository = relationRepository,
            aliasDao = aliasDao,
            tagDao = tagDao,
            detailsMetadataDao = detailsMetadataDao,
            lookupApi = lookupApi,
            coroutineDispatchers = coroutineDispatchers,
            musicBrainzAuthStore = musicBrainzAuthStore,
        )
    }
}
