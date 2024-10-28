package ly.david.musicsearch.data.repository.search

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import ly.david.data.test.api.FakeSearchApi
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.SearchResultDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.data.musicbrainz.api.SearchApi
import ly.david.musicsearch.data.musicbrainz.api.SearchAreasResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchArtistsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.repository.KoinTestRule
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.listitem.EndOfList
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class SearchResultsRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val searchResultDao: SearchResultDao by inject()
    private val areaDao: AreaDao by inject()
    private val artistDao: ArtistDao by inject()
    private val eventDao: EventDao by inject()
    private val instrumentDao: InstrumentDao by inject()
    private val labelDao: LabelDao by inject()
    private val placeDao: PlaceDao by inject()
    private val recordingDao: RecordingDao by inject()
    private val releaseDao: ReleaseDao by inject()
    private val releaseGroupDao: ReleaseGroupDao by inject()
    private val seriesDao: SeriesDao by inject()
    private val workDao: WorkDao by inject()

    private fun createRepositoryWithFakeNetworkData(
        searchApi: SearchApi,
    ): SearchResultsRepositoryImpl {
        return SearchResultsRepositoryImpl(
            searchApi = searchApi,
            searchResultDao = searchResultDao,
            areaDao = areaDao,
            artistDao = artistDao,
            eventDao = eventDao,
            instrumentDao = instrumentDao,
            labelDao = labelDao,
            placeDao = placeDao,
            recordingDao = recordingDao,
            releaseDao = releaseDao,
            releaseGroupDao = releaseGroupDao,
            seriesDao = seriesDao,
            workDao = workDao,
        )
    }

    @Test
    fun `empty network, no list items`() = runTest {
        val repository = createRepositoryWithFakeNetworkData(
            searchApi = FakeSearchApi(),
        )

        val flow: Flow<PagingData<ListItemModel>> = repository.observeSearchResults(
            entity = MusicBrainzEntity.ARTIST,
            query = "a",
        )
        val searchResults: List<ListItemModel> = flow.asSnapshot()

        Assert.assertEquals(
            0,
            searchResults.size,
        )
    }

    @Test
    fun `one more list item is returned as footer`() = runTest {
        val repository = createRepositoryWithFakeNetworkData(
            searchApi = object : FakeSearchApi() {
                override suspend fun queryArtists(
                    query: String,
                    limit: Int,
                    offset: Int,
                ): SearchArtistsResponse {
                    return SearchArtistsResponse(
                        count = 1,
                        artists = listOf(
                            ArtistMusicBrainzModel(
                                id = "1",
                                name = "Various Artists",
                            ),
                        ),
                    )
                }
            },
        )

        val flow: Flow<PagingData<ListItemModel>> = repository.observeSearchResults(
            entity = MusicBrainzEntity.ARTIST,
            query = "a",
        )
        val searchResults: List<ListItemModel> = flow.asSnapshot()

        Assert.assertEquals(
            2,
            searchResults.size,
        )

        val footer = searchResults.last()
        Assert.assertEquals(
            footer,
            EndOfList,
        )
    }

    @Test
    fun `results are ordered`() = runTest {
        val sut = createRepositoryWithFakeNetworkData(
            searchApi = object : FakeSearchApi() {
                override suspend fun queryArtists(
                    query: String,
                    limit: Int,
                    offset: Int,
                ): SearchArtistsResponse {
                    return SearchArtistsResponse(
                        count = 4,
                        artists = listOf(
                            ArtistMusicBrainzModel(
                                id = "89ad4ac3-39f7-470e-963a-56509c546377",
                                name = "Various Artists",
                            ),
                            ArtistMusicBrainzModel(
                                id = "b972f589-fb0e-474e-b64a-803b0364fa75",
                                name = "Wolfgang Amadeus Mozart",
                            ),
                            ArtistMusicBrainzModel(
                                id = "125ec42a-7229-4250-afc5-e057484327fe",
                                name = "[unknown]",
                            ),
                            ArtistMusicBrainzModel(
                                id = "7364dea6-ca9a-48e3-be01-b44ad0d19897",
                                name = "a-ha",
                            ),
                        ),
                    )
                }
            },
        )

        val flow: Flow<PagingData<ListItemModel>> = sut.observeSearchResults(
            entity = MusicBrainzEntity.ARTIST,
            query = "a",
        )
        val searchResults: List<ListItemModel> = flow.asSnapshot()

        Assert.assertEquals(
            5,
            searchResults.size,
        )
        Assert.assertEquals(
            "Various Artists",
            (searchResults[0] as ArtistListItemModel).name,
        )
        Assert.assertEquals(
            "Wolfgang Amadeus Mozart",
            (searchResults[1] as ArtistListItemModel).name,
        )
        Assert.assertEquals(
            "[unknown]",
            (searchResults[2] as ArtistListItemModel).name,
        )
        Assert.assertEquals(
            "a-ha",
            (searchResults[3] as ArtistListItemModel).name,
        )
    }

    @Test
    fun `changing query will not have old results show up`() = runTest {
        val sut = createRepositoryWithFakeNetworkData(
            searchApi = object : FakeSearchApi() {
                override suspend fun queryAreas(
                    query: String,
                    limit: Int,
                    offset: Int,
                ): SearchAreasResponse {
                    return SearchAreasResponse(
                        count = 1,
                        areas = listOf(
                            AreaMusicBrainzModel(
                                id = "f42c1e2a-b7db-4acf-9742-1889b9c6d530",
                                name = "A Coruña",
                            ),
                        ),
                    )
                }

                override suspend fun queryArtists(
                    query: String,
                    limit: Int,
                    offset: Int,
                ): SearchArtistsResponse {
                    return SearchArtistsResponse(
                        count = 1,
                        artists = listOf(
                            ArtistMusicBrainzModel(
                                id = "89ad4ac3-39f7-470e-963a-56509c546377",
                                name = "Various Artists",
                            ),
                        ),
                    )
                }
            },
        )

        var flow: Flow<PagingData<ListItemModel>> = sut.observeSearchResults(
            entity = MusicBrainzEntity.ARTIST,
            query = "a",
        )
        var searchResults: List<ListItemModel> = flow.asSnapshot()
        Assert.assertEquals(
            2,
            searchResults.size,
        )
        Assert.assertEquals(
            "Various Artists",
            (searchResults[0] as ArtistListItemModel).name,
        )

        flow = sut.observeSearchResults(
            entity = MusicBrainzEntity.AREA,
            query = "a",
        )
        searchResults = flow.asSnapshot()
        Assert.assertEquals(
            2,
            searchResults.size,
        )
        Assert.assertEquals(
            "A Coruña",
            (searchResults[0] as AreaListItemModel).name,
        )
    }
}
