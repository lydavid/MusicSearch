package ly.david.musicsearch.data.repository.instrument

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.preferences.NoOpMusicBrainzAuthStore
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.musicbrainz.api.GENERAL_LOOKUP_INCLUDES
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.api.USER_LOOKUP_INCLUDES
import ly.david.musicsearch.data.musicbrainz.models.UrlMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.TagMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.repository.helpers.TestInstrumentRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.InstrumentDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.instrument.InstrumentType
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.tag.GenreChip
import ly.david.musicsearch.shared.domain.tag.TagChip
import ly.david.musicsearch.shared.domain.tag.VoteType
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.time.Duration.Companion.seconds

private const val ID = "43f378cf-b099-46da-8ec3-a39b6f5e5258"

class InstrumentRepositoryImplTest : KoinTest, TestInstrumentRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val relationsMetadataDao: RelationsMetadataDao by inject()
    override val detailsMetadataDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()
    override val instrumentDao: InstrumentDao by inject()
    override val aliasDao: AliasDao by inject()
    override val tagDao: TagDao by inject()
    override val coroutineDispatchers: CoroutineDispatchers by inject()

    override val lookupApi: LookupApi = mockk()

    private val musicBrainzModel = InstrumentMusicBrainzNetworkModel(
        id = ID,
        name = "classical guitar",
    )

    @Test
    fun `include user lookup includes only if user has all auth scopes`() = runTest {
        coEvery {
            lookupApi.lookupInstrument(
                instrumentId = any(),
                include = GENERAL_LOOKUP_INCLUDES,
            )
        } returns musicBrainzModel
        coEvery {
            lookupApi.lookupInstrument(
                instrumentId = any(),
                include = "$GENERAL_LOOKUP_INCLUDES+$USER_LOOKUP_INCLUDES",
            )
        } returns musicBrainzModel.copy(
            genres = listOf(
                GenreMusicBrainzNetworkModel(
                    id = "6ed4e4d1-9a97-4e2c-b8df-083754f154f4",
                    name = "classical",
                    count = 10,
                ),
            ),
            userGenres = listOf(
                GenreMusicBrainzNetworkModel(
                    id = "6ed4e4d1-9a97-4e2c-b8df-083754f154f4",
                    name = "classical",
                ),
            ),
            tags = listOf(
                TagMusicBrainzNetworkModel(
                    name = "classical",
                    count = 10,
                ),
                TagMusicBrainzNetworkModel(
                    name = "guitar",
                    count = 1,
                ),
                TagMusicBrainzNetworkModel(
                    name = "strings",
                    count = 1,
                ),
            ),
            userTags = listOf(
                TagMusicBrainzNetworkModel(
                    name = "classical",
                ),
                TagMusicBrainzNetworkModel(
                    name = "strings",
                ),
            ),
        )
        val repositoryWithoutAllScopes = createInstrumentRepository(
            musicBrainzAuthStore = NoOpMusicBrainzAuthStore(),
        )
        repositoryWithoutAllScopes.lookupEntity(
            entityId = ID,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).run {
            assertEquals(
                InstrumentDetailsModel(
                    id = ID,
                    name = "classical guitar",
                    lastUpdated = testDateTimeInThePast,
                ),
                this,
            )
        }

        val repositoryWithAllScopes = createInstrumentRepository(
            musicBrainzAuthStore = object : NoOpMusicBrainzAuthStore() {
                override suspend fun userHasAllAuthScopes(): Boolean {
                    return true
                }
            },
        )
        repositoryWithAllScopes.lookupEntity(
            entityId = ID,
            forceRefresh = true,
            lastUpdated = testDateTimeInThePast,
        ).run {
            assertEquals(
                InstrumentDetailsModel(
                    id = ID,
                    name = "classical guitar",
                    lastUpdated = testDateTimeInThePast,
                    genres = persistentListOf(
                        GenreChip(
                            id = "6ed4e4d1-9a97-4e2c-b8df-083754f154f4",
                            name = "classical",
                            count = 10,
                            voteType = VoteType.Upvote,
                        ),
                    ),
                    tags = persistentListOf(
                        TagChip(
                            name = "guitar",
                            count = 1,
                        ),
                        TagChip(
                            name = "strings",
                            count = 1,
                            voteType = VoteType.Upvote,
                        ),
                    ),
                ),
                this,
            )
        }
    }

    @Test
    fun `lookup is cached, and force refresh invalidates cache`() = runTest {
        coEvery { lookupApi.lookupInstrument(any(), any()) } returns musicBrainzModel
        val sparseRepository = createInstrumentRepository()
        val sparseDetailsModel = sparseRepository.lookupEntity(
            entityId = ID,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        assertEquals(
            InstrumentDetailsModel(
                id = ID,
                name = "classical guitar",
                lastUpdated = testDateTimeInThePast,
            ),
            sparseDetailsModel,
        )

        coEvery { lookupApi.lookupInstrument(any(), any()) } returns musicBrainzModel.copy(
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
        )
        val allDataRepository = createInstrumentRepository()
        var allDataArtistDetailsModel = allDataRepository.lookupEntity(
            entityId = ID,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast.plus(1.seconds),
        )
        assertEquals(
            InstrumentDetailsModel(
                id = ID,
                name = "classical guitar",
                lastUpdated = testDateTimeInThePast,
            ),
            allDataArtistDetailsModel,
        )
        allDataArtistDetailsModel = allDataRepository.lookupEntity(
            entityId = ID,
            forceRefresh = true,
            lastUpdated = testDateTimeInThePast.plus(2.seconds),
        )
        assertEquals(
            InstrumentDetailsModel(
                id = ID,
                name = "classical guitar",
                description = "Also known as Spanish guitar, it is used in classical, folk and other styles, the strings are nylon or gut.",
                disambiguation = "Modern acoustic gut/nylon string guitar",
                type = InstrumentType.StringInstrument,
                lastUpdated = testDateTimeInThePast.plus(2.seconds),
                urls = persistentListOf(
                    RelationListItemModel(
                        id = "a_7",
                        linkedEntityId = "a",
                        type = "Wikidata",
                        name = "https://www.wikidata.org/wiki/Q719120",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = false,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "a_5",
                        linkedEntityId = "a",
                        type = "other databases",
                        name = "https://saisaibatake.ame-zaiku.com/gakki/guitar/gakki_guitar_classic.html",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = false,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "a_6",
                        linkedEntityId = "a",
                        type = "other databases",
                        name = "https://saisaibatake.ame-zaiku.com/gakki/guitar/gakki_guitar_gut.html",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = false,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "a_4",
                        linkedEntityId = "a",
                        type = "picture",
                        name = "https://static.metabrainz.org/irombook/guitar/guitar_acoustic_classical.png",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = false,
                        isForwardDirection = true,
                    ),
                ),
            ),
            allDataArtistDetailsModel,
        )
    }
}
