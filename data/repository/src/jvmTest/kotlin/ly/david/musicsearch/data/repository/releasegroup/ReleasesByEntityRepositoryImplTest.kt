package ly.david.musicsearch.data.repository.releasegroup

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import ly.david.data.test.api.FakeMusicBrainzApi
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
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
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

    // Copy and paste the SUT's parameters
    // Inject for all DAOs
    // Swap out network APIs for fake
    private val database: Database by inject()
    private val artistReleaseDao: ArtistReleaseDao by inject()
    private val browseEntityCountDao: BrowseEntityCountDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()
    private val musicBrainzApi: MusicBrainzApi = FakeMusicBrainzApi()
    private val recordingReleaseDao: RecordingReleaseDao by inject()
    private val releaseDao: ReleaseDao by inject()
    private val releaseCountryDao: ReleaseCountryDao by inject()
    private val releaseLabelDao: ReleaseLabelDao by inject()
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao by inject()

    @Test
    fun `release with multiple catalog numbers`() = runTest {
        val repository = ReleasesByEntityRepositoryImpl(
            artistReleaseDao = artistReleaseDao,
            browseEntityCountDao = browseEntityCountDao,
            collectionEntityDao = collectionEntityDao,
            musicBrainzApi = musicBrainzApi,
            recordingReleaseDao = recordingReleaseDao,
            releaseDao = releaseDao,
            releaseCountryDao = releaseCountryDao,
            releaseLabelDao = releaseLabelDao,
            releaseReleaseGroupDao = releaseReleaseGroupDao,
        )

        // TODO: use repository?
        database.labelQueries.insert(
            Label(
                id = "7689c51f-e09e-4e85-80d0-b95a9e23d216",
                name = "",
                disambiguation = "",
                type = null,
                type_id = "",
                label_code = 0,
                begin = "",
                ended = null,
                end = "",
            ),
        )

        val flow: Flow<PagingData<ReleaseListItemModel>> = repository.observeReleasesByEntity(
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
