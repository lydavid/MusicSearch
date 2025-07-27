package ly.david.musicsearch.data.repository.area

import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.helpers.TestAreaRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.shared.domain.area.AreaType.COUNTRY
import ly.david.musicsearch.shared.domain.details.AreaDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.time.Duration.Companion.minutes

class AreaRepositoryImplTest : KoinTest, TestAreaRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val relationsMetadataDao: RelationsMetadataDao by inject()
    override val detailsMetadataDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()
    override val areaDao: AreaDao by inject()
    override val aliasDao: AliasDao by inject()

    @Test
    fun `lookup is cached, and force refresh invalidates cache`() = runTest {
        val sparseRepository = createAreaRepository(
            musicBrainzModel = AreaMusicBrainzNetworkModel(
                id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                name = "South Georgia and the South Sandwich Islands",
            ),
        )
        val sparseDetailsModel = sparseRepository.lookupArea(
            areaId = "38ce2215-162b-3f3c-af41-34800017e1d8",
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        assertEquals(
            AreaDetailsModel(
                id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                name = "South Georgia and the South Sandwich Islands",
                lastUpdated = testDateTimeInThePast,
            ),
            sparseDetailsModel,
        )

        val allDataRepository = createAreaRepository(
            musicBrainzModel = AreaMusicBrainzNetworkModel(
                id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                name = "South Georgia and the South Sandwich Islands",
                type = COUNTRY,
                countryCodes = listOf("GS"),
            ),
        )
        var allDataArtistDetailsModel = allDataRepository.lookupArea(
            areaId = "38ce2215-162b-3f3c-af41-34800017e1d8",
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast.plus(1.minutes),
        )
        assertEquals(
            AreaDetailsModel(
                id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                name = "South Georgia and the South Sandwich Islands",
                lastUpdated = testDateTimeInThePast, // not updated without refreshing
            ),
            allDataArtistDetailsModel,
        )
        allDataArtistDetailsModel = allDataRepository.lookupArea(
            areaId = "38ce2215-162b-3f3c-af41-34800017e1d8",
            forceRefresh = true,
            lastUpdated = testDateTimeInThePast.plus(2.minutes),
        )
        assertEquals(
            AreaDetailsModel(
                id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                name = "South Georgia and the South Sandwich Islands",
                type = COUNTRY,
                countryCode = "GS",
                lastUpdated = testDateTimeInThePast.plus(2.minutes),
            ),
            allDataArtistDetailsModel,
        )
    }
}
