package ly.david.musicsearch.data.repository.releasegroup

import kotlinx.coroutines.runBlocking
import ly.david.data.test.api.FakeMusicBrainzApi
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class ReleasesByEntityRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val artistReleaseDao: ArtistReleaseDao by inject()
    private val browseEntityCountDao: BrowseEntityCountDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()
    private val musicBrainzApi: MusicBrainzApi = FakeMusicBrainzApi()
    private val recordingReleaseDao: RecordingReleaseDao by inject()
    private val releaseDao: ReleaseDao by inject()
    private val releaseCountryDao: ReleaseCountryDao by inject()
    private val releaseLabelDao: ReleaseLabelDao by inject()
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao by inject()

    @Before
    fun setup() {
    }

    @Test
    fun e() = runBlocking {
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
        assertEquals(
            "",
            repository.browseEntities(
                "",
                MusicBrainzEntity.LABEL,
                0
            ),
        )
    }
}
