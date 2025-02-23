package ly.david.musicsearch.data.repository.area

import kotlinx.coroutines.test.runTest
import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.data.test.KoinTestRule
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.shared.domain.area.AreaDetailsModel
import ly.david.musicsearch.shared.domain.area.AreaRepository
import ly.david.musicsearch.shared.domain.area.AreaType.COUNTRY
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class AreaRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val entityHasRelationsDao: EntityHasRelationsDao by inject()
    private val visitedDao: VisitedDao by inject()
    private val relationDao: RelationDao by inject()
    private val areaDao: AreaDao by inject()

    private fun createRepository(
        musicBrainzModel: AreaMusicBrainzModel,
    ): AreaRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupArea(
                    areaId: String,
                    include: String?,
                ): AreaMusicBrainzModel {
                    return musicBrainzModel
                }
            },
            entityHasRelationsDao = entityHasRelationsDao,
            visitedDao = visitedDao,
            relationDao = relationDao,
        )
        return AreaRepositoryImpl(
            areaDao = areaDao,
            relationRepository = relationRepository,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupArea(
                    areaId: String,
                    include: String?,
                ): AreaMusicBrainzModel {
                    return musicBrainzModel
                }
            },
        )
    }

    @Test
    fun `lookup is cached, and force refresh invalidates cache`() = runTest {
        val sparseRepository = createRepository(
            musicBrainzModel = AreaMusicBrainzModel(
                id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                name = "South Georgia and the South Sandwich Islands",
            ),
        )
        val sparseDetailsModel = sparseRepository.lookupArea(
            areaId = "38ce2215-162b-3f3c-af41-34800017e1d8",
            forceRefresh = false,
        )
        assertEquals(
            AreaDetailsModel(
                id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                name = "South Georgia and the South Sandwich Islands",
            ),
            sparseDetailsModel,
        )

        val allDataRepository = createRepository(
            musicBrainzModel = AreaMusicBrainzModel(
                id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                name = "South Georgia and the South Sandwich Islands",
                type = COUNTRY,
                countryCodes = listOf("GS"),
            ),
        )
        var allDataArtistDetailsModel = allDataRepository.lookupArea(
            areaId = "38ce2215-162b-3f3c-af41-34800017e1d8",
            forceRefresh = false,
        )
        assertEquals(
            AreaDetailsModel(
                id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                name = "South Georgia and the South Sandwich Islands",
            ),
            allDataArtistDetailsModel,
        )
        allDataArtistDetailsModel = allDataRepository.lookupArea(
            areaId = "38ce2215-162b-3f3c-af41-34800017e1d8",
            forceRefresh = true,
        )
        assertEquals(
            AreaDetailsModel(
                id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                name = "South Georgia and the South Sandwich Islands",
                type = COUNTRY,
                countryCodes = listOf("GS"),
            ),
            allDataArtistDetailsModel,
        )
    }
}
