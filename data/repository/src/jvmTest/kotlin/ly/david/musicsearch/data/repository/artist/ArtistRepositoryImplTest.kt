package ly.david.musicsearch.data.repository.artist

import kotlinx.coroutines.test.runTest
import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.EntityHasUrlsDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.repository.KoinTestRule
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.artist.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class ArtistRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val artistDao: ArtistDao by inject()
    private val entityHasRelationsDao: EntityHasRelationsDao by inject()
    private val entityHasUrlsDao: EntityHasUrlsDao by inject()
    private val relationDao: RelationDao by inject()
    private val areaDao: AreaDao by inject()

    private fun createRepositoryWithFakeNetworkData(
        artistMusicBrainzModel: ArtistMusicBrainzModel,
    ): ArtistRepositoryImpl {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupArtist(
                    artistId: String,
                    include: String?,
                ): ArtistMusicBrainzModel {
                    return artistMusicBrainzModel
                }
            },
            entityHasRelationsDao = entityHasRelationsDao,
            entityHasUrlsDao = entityHasUrlsDao,
            relationDao = relationDao,
        )
        return ArtistRepositoryImpl(
            artistDao = artistDao,
            relationRepository = relationRepository,
            areaDao = areaDao,
            lookupArtistApi = object : FakeLookupApi() {
                override suspend fun lookupArtist(
                    artistId: String,
                    include: String?,
                ): ArtistMusicBrainzModel {
                    return artistMusicBrainzModel
                }
            },
        )
    }

    @Test
    fun `lookup artist`() = runTest {
        val sut = createRepositoryWithFakeNetworkData(
            artistMusicBrainzModel = ArtistMusicBrainzModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
                type = "Group",
                lifeSpan = LifeSpanMusicBrainzModel(
                    begin = "1960",
                    end = "1970-04-10",
                    ended = true,
                ),
                sortName = "Beatles, The",
                area = AreaMusicBrainzModel(
                    id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                    name = "United Kingdom",
                    countryCodes = listOf("GB"),
                ),
            ),
        )

        val artistDetailsModel = sut.lookupArtistDetails("b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d", false)
        assertEquals(
            ArtistDetailsModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
                type = "Group",
                lifeSpan = LifeSpanUiModel(
                    begin = "1960",
                    end = "1970-04-10",
                    ended = true,
                ),
                sortName = "Beatles, The",
                areaListItemModel = AreaListItemModel(
                    id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                    name = "United Kingdom",
                    countryCodes = listOf("GB"),
                ),
            ),
            artistDetailsModel,
        )
    }
}
