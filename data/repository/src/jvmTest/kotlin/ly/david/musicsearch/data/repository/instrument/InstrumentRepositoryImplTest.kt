package ly.david.musicsearch.data.repository.instrument

import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.musicbrainz.models.UrlMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.shared.domain.details.InstrumentDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.instrument.InstrumentRepository
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.time.Duration.Companion.seconds

class InstrumentRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val relationsMetadataDao: RelationsMetadataDao by inject()
    private val detailsMetadataDao: DetailsMetadataDao by inject()
    private val relationDao: RelationDao by inject()
    private val instrumentDao: InstrumentDao by inject()
    private val aliasDao: AliasDao by inject()

    private fun createRepository(
        musicBrainzModel: InstrumentMusicBrainzNetworkModel,
    ): InstrumentRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupInstrument(
                    instrumentId: String,
                    include: String,
                ): InstrumentMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
            relationsMetadataDao = relationsMetadataDao,
            detailsMetadataDao = detailsMetadataDao,
            relationDao = relationDao,
        )
        return InstrumentRepositoryImpl(
            instrumentDao = instrumentDao,
            relationRepository = relationRepository,
            aliasDao = aliasDao,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupInstrument(
                    instrumentId: String,
                    include: String,
                ): InstrumentMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
        )
    }

    @Test
    fun `lookup is cached, and force refresh invalidates cache`() = runTest {
        val sparseRepository = createRepository(
            musicBrainzModel = InstrumentMusicBrainzNetworkModel(
                id = "43f378cf-b099-46da-8ec3-a39b6f5e5258",
                name = "classical guitar",
            ),
        )
        val sparseDetailsModel = sparseRepository.lookupInstrument(
            instrumentId = "43f378cf-b099-46da-8ec3-a39b6f5e5258",
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        assertEquals(
            InstrumentDetailsModel(
                id = "43f378cf-b099-46da-8ec3-a39b6f5e5258",
                name = "classical guitar",
                lastUpdated = testDateTimeInThePast,
            ),
            sparseDetailsModel,
        )

        val allDataRepository = createRepository(
            musicBrainzModel = InstrumentMusicBrainzNetworkModel(
                id = "43f378cf-b099-46da-8ec3-a39b6f5e5258",
                name = "classical guitar",
                description = "Also known as Spanish guitar, it is used in classical, folk and other styles, the strings are nylon or gut.",
                disambiguation = "Modern acoustic gut/nylon string guitar",
                type = "String instrument",
                relations = listOf(
                    RelationMusicBrainzModel(
                        type = "image",
                        typeId = "f64eacbd-1ea1-381e-9886-2cfb552b7d90",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://static.metabrainz.org/irombook/guitar/guitar_acoustic_classical.png",
                            id = "a",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "other databases",
                        typeId = "41930af2-cb94-488d-a4f0-d232f6ef391a",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://saisaibatake.ame-zaiku.com/gakki/guitar/gakki_guitar_classic.html",
                            id = "a",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "other databases",
                        typeId = "41930af2-cb94-488d-a4f0-d232f6ef391a",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://saisaibatake.ame-zaiku.com/gakki/guitar/gakki_guitar_gut.html",
                            id = "a",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "wikidata",
                        typeId = "1486fccd-cf59-35e4-9399-b50e2b255877",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://www.wikidata.org/wiki/Q719120",
                            id = "a",
                        ),
                    ),
                ),
            ),
        )
        var allDataArtistDetailsModel = allDataRepository.lookupInstrument(
            instrumentId = "43f378cf-b099-46da-8ec3-a39b6f5e5258",
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast.plus(1.seconds),
        )
        assertEquals(
            InstrumentDetailsModel(
                id = "43f378cf-b099-46da-8ec3-a39b6f5e5258",
                name = "classical guitar",
                lastUpdated = testDateTimeInThePast,
            ),
            allDataArtistDetailsModel,
        )
        allDataArtistDetailsModel = allDataRepository.lookupInstrument(
            instrumentId = "43f378cf-b099-46da-8ec3-a39b6f5e5258",
            forceRefresh = true,
            lastUpdated = testDateTimeInThePast.plus(2.seconds),
        )
        assertEquals(
            InstrumentDetailsModel(
                id = "43f378cf-b099-46da-8ec3-a39b6f5e5258",
                name = "classical guitar",
                description = "Also known as Spanish guitar, it is used in classical, folk and other styles, the strings are nylon or gut.",
                disambiguation = "Modern acoustic gut/nylon string guitar",
                type = "String instrument",
                lastUpdated = testDateTimeInThePast.plus(2.seconds),
                urls = listOf(
                    RelationListItemModel(
                        id = "a_7",
                        linkedEntityId = "a",
                        label = "Wikidata",
                        name = "https://www.wikidata.org/wiki/Q719120",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "a_5",
                        linkedEntityId = "a",
                        label = "other databases",
                        name = "https://saisaibatake.ame-zaiku.com/gakki/guitar/gakki_guitar_classic.html",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "a_6",
                        linkedEntityId = "a",
                        label = "other databases",
                        name = "https://saisaibatake.ame-zaiku.com/gakki/guitar/gakki_guitar_gut.html",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "a_4",
                        linkedEntityId = "a",
                        label = "picture",
                        name = "https://static.metabrainz.org/irombook/guitar/guitar_acoustic_classical.png",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                ),
            ),
            allDataArtistDetailsModel,
        )
    }
}
