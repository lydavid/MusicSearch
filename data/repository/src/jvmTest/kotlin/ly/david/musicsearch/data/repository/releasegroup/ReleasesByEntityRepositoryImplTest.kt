package ly.david.musicsearch.data.repository.releasegroup

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import ly.david.data.test.releaseWith3CatalogNumbersWithSameLabel
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.dao.ArtistReleaseDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.RecordingReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseCountryDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseLabelDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleaseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.musicsearch.data.repository.release.ReleasesByEntityRepositoryImpl
import lydavidmusicsearchdatadatabase.Label
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class ReleasesByEntityRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val database: Database by inject()
    private val artistReleaseDao: ArtistReleaseDao by inject()
    private val browseEntityCountDao: BrowseEntityCountDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()
    private val recordingReleaseDao: RecordingReleaseDao by inject()
    private val releaseDao: ReleaseDao by inject()
    private val releaseCountryDao: ReleaseCountryDao by inject()
    private val releaseLabelDao: ReleaseLabelDao by inject()
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao by inject()

    @Test
    fun `releases by label - release with multiple catalog numbers`() = runTest {
        val sut = ReleasesByEntityRepositoryImpl(
            artistReleaseDao = artistReleaseDao,
            browseEntityCountDao = browseEntityCountDao,
            collectionEntityDao = collectionEntityDao,
            browseReleaseApi = object : BrowseReleaseApi {
                override suspend fun browseReleasesByEntity(
                    entityId: String,
                    entity: MusicBrainzEntity,
                    limit: Int,
                    offset: Int,
                    include: String,
                ) = BrowseReleasesResponse(
                    count = 1,
                    offset = 0,
                    musicBrainzModels = listOf(
                        releaseWith3CatalogNumbersWithSameLabel,
                    ),
                )
            },
            recordingReleaseDao = recordingReleaseDao,
            releaseDao = releaseDao,
            releaseCountryDao = releaseCountryDao,
            releaseLabelDao = releaseLabelDao,
            releaseReleaseGroupDao = releaseReleaseGroupDao,
        )

        // Although I could use the label repository here, it will require standing up more fakes
        // I feel like it will pollute the test case more than its worth
        database.labelQueries.insert(
            Label(
                id = "7689c51f-e09e-4e85-80d0-b95a9e23d216",
                name = "Virgin Music",
                disambiguation = "a division of Universal Music Japan created in 2014 that replaces EMI R",
                type = "Original Production",
                type_id = "7aaa37fe-2def-3476-b359-80245850062d",
                label_code = null,
                begin = null,
                ended = null,
                end = null,
            ),
        )

        val flow: Flow<PagingData<ReleaseListItemModel>> = sut.observeReleasesByEntity(
            entityId = "7689c51f-e09e-4e85-80d0-b95a9e23d216",
            MusicBrainzEntity.LABEL,
            listFilters = ListFilters(
                query = "ウタ",
            ),
        )
        val releases: List<ReleaseListItemModel> = flow.asSnapshot()

        assertEquals(
            1,
            releases.size,
        )
        val release: ReleaseListItemModel = releases[0]
        assertEquals(
            "ウタの歌 ONE PIECE FILM RED",
            release.name,
        )
        assertEquals(
            "TYBX-10260, TYCT-69245, TYCX-60187",
            release.catalogNumbers,
        )
    }
}
