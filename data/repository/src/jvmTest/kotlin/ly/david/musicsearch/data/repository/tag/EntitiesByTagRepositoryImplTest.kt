package ly.david.musicsearch.data.repository.tag

import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.clock.FixedClock
import ly.david.data.test.davidBowieArtistMusicBrainzModel
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.TagMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.shared.domain.listitem.TaggedEntityListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.tag.TagRepository
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class EntitiesByTagRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val artistDao: ArtistDao by inject()
    private val tagDao: TagDao by inject()

    private val artistId = davidBowieArtistMusicBrainzModel.id

    private fun createRepository(): TagRepository = TagRepositoryImpl(
        tagApi = io.mockk.mockk(relaxed = true),
        tagDao = tagDao,
        musicBrainzAuthStore = io.mockk.mockk(relaxed = true),
        clock = FixedClock(now = testDateTimeInThePast),
    )

    private fun seedArtistWithTagsAndGenres() {
        artistDao.upsert(
            oldId = "",
            artist = davidBowieArtistMusicBrainzModel,
        )
        tagDao.insertAll(
            entityId = artistId,
            genres = listOf(
                GenreMusicBrainzNetworkModel(
                    id = "54d89e62-5bfb-42bc-a321-9230e6fdcd75",
                    name = "glam rock",
                    count = 22,
                ),
            ),
            userGenres = null,
            tags = listOf(
                TagMusicBrainzNetworkModel(
                    name = "british",
                    count = 6,
                ),
                TagMusicBrainzNetworkModel(
                    name = "uk",
                    count = 6,
                ),
                TagMusicBrainzNetworkModel(
                    name = "some spam tag",
                    count = 99,
                ),
            ),
            userTags = null,
        )
    }

    private val expectedArtist = TaggedEntityListItemModel(
        id = artistId,
        entityType = MusicBrainzEntityType.ARTIST,
        entityName = "David Bowie",
        disambiguation = "",
    )

    @Test
    fun `entities by tag`() = runTest {
        seedArtistWithTagsAndGenres()

        val tagRepository = createRepository()

        testFilter(
            pagingFlowProducer = { query ->
                tagRepository.observeEntitiesByTagOrGenre(
                    name = "british",
                    query = query,
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(expectedArtist),
                ),
                FilterTestCase(
                    description = "filter by entity name",
                    query = "david",
                    expectedResult = listOf(expectedArtist),
                ),
                FilterTestCase(
                    description = "no match",
                    query = "does not match",
                    expectedResult = emptyList(),
                ),
            ),
        )
    }

    @Test
    fun `entities by genre`() = runTest {
        seedArtistWithTagsAndGenres()

        val tagRepository = createRepository()

        testFilter(
            pagingFlowProducer = { query ->
                tagRepository.observeEntitiesByTagOrGenre(
                    name = "glam rock",
                    query = query,
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(expectedArtist),
                ),
                FilterTestCase(
                    description = "no match",
                    query = "does not match",
                    expectedResult = emptyList(),
                ),
            ),
        )
    }
}
