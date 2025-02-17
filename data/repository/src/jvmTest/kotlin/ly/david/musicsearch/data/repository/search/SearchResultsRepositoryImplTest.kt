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
import ly.david.musicsearch.data.musicbrainz.api.SearchEventsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchReleasesResponse
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.data.test.KoinTestRule
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.listitem.EndOfList
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.listitem.SearchHeader
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
            3,
            searchResults.size,
        )
        Assert.assertEquals(
            SearchHeader(
                remoteCount = 1,
            ),
            searchResults.first(),
        )
        Assert.assertEquals(
            EndOfList,
            searchResults.last(),
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
            6,
            searchResults.size,
        )
        Assert.assertEquals(
            ArtistListItemModel(
                id = "89ad4ac3-39f7-470e-963a-56509c546377",
                name = "Various Artists",
            ),
            (searchResults[1] as ArtistListItemModel),
        )
        Assert.assertEquals(
            ArtistListItemModel(
                id = "b972f589-fb0e-474e-b64a-803b0364fa75",
                name = "Wolfgang Amadeus Mozart",
            ),
            (searchResults[2] as ArtistListItemModel),
        )
        Assert.assertEquals(
            ArtistListItemModel(
                id = "125ec42a-7229-4250-afc5-e057484327fe",
                name = "[unknown]",
            ),
            (searchResults[3] as ArtistListItemModel),
        )
        Assert.assertEquals(
            ArtistListItemModel(
                id = "7364dea6-ca9a-48e3-be01-b44ad0d19897",
                name = "a-ha",
            ),
            (searchResults[4] as ArtistListItemModel),
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
            3,
            searchResults.size,
        )
        Assert.assertEquals(
            ArtistListItemModel(
                id = "89ad4ac3-39f7-470e-963a-56509c546377",
                name = "Various Artists",
            ),
            (searchResults[1] as ArtistListItemModel),
        )

        flow = sut.observeSearchResults(
            entity = MusicBrainzEntity.AREA,
            query = "a",
        )
        searchResults = flow.asSnapshot()
        Assert.assertEquals(
            3,
            searchResults.size,
        )
        Assert.assertEquals(
            AreaListItemModel(
                id = "f42c1e2a-b7db-4acf-9742-1889b9c6d530",
                name = "A Coruña",
            ),
            (searchResults[1] as AreaListItemModel),
        )
    }

    // This shouldn't be a problem as long as our LazyColumn is not keyed
    @Test
    fun `duplicates are shown`() = runTest {
        val repository = createRepositoryWithFakeNetworkData(
            searchApi = object : FakeSearchApi() {
                override suspend fun queryEvents(
                    query: String,
                    limit: Int,
                    offset: Int,
                ): SearchEventsResponse {
                    return SearchEventsResponse(
                        count = 2,
                        events = listOf(
                            EventMusicBrainzModel(
                                id = "1bc74971-d5c8-4a21-b761-c24e74efb9b4",
                                name = "Lollapalooza 2024",
                            ),
                            EventMusicBrainzModel(
                                id = "1bc74971-d5c8-4a21-b761-c24e74efb9b4",
                                name = "Lollapalooza 2024",
                            ),
                        ),
                    )
                }
            },
        )

        val flow: Flow<PagingData<ListItemModel>> = repository.observeSearchResults(
            entity = MusicBrainzEntity.EVENT,
            query = "a",
        )
        val searchResults: List<ListItemModel> = flow.asSnapshot()

        Assert.assertEquals(
            4,
            searchResults.size,
        )
        Assert.assertEquals(
            EventListItemModel(
                id = "1bc74971-d5c8-4a21-b761-c24e74efb9b4",
                name = "Lollapalooza 2024",
            ),
            (searchResults[1] as EventListItemModel),
        )
        Assert.assertEquals(
            EventListItemModel(
                id = "1bc74971-d5c8-4a21-b761-c24e74efb9b4",
                name = "Lollapalooza 2024",
            ),
            (searchResults[2] as EventListItemModel),
        )
        Assert.assertEquals(
            EndOfList,
            searchResults.last(),
        )
    }

    @Test
    fun `release must include artist credits`() = runTest {
        val repository = createRepositoryWithFakeNetworkData(
            searchApi = object : FakeSearchApi() {
                override suspend fun queryReleases(
                    query: String,
                    limit: Int,
                    offset: Int,
                ): SearchReleasesResponse {
                    return SearchReleasesResponse(
                        count = 1,
                        releases = listOf(
                            ReleaseMusicBrainzModel(
                                id = "0bc23883-16c2-4f9b-b3f5-b3685e530435",
                                name = "Keep The Beats!",
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        artist = ArtistMusicBrainzModel(
                                            id = "122cce39-8303-4801-989e-cefa438bd98d",
                                            name = "Girls Dead Monster",
                                        ),
                                        name = "Girls Dead Monster",
                                    ),
                                ),
                            ),
                        ),
                    )
                }
            },
        )

        val flow: Flow<PagingData<ListItemModel>> = repository.observeSearchResults(
            entity = MusicBrainzEntity.RELEASE,
            query = "a",
        )
        val searchResults: List<ListItemModel> = flow.asSnapshot()

        Assert.assertEquals(
            3,
            searchResults.size,
        )
        Assert.assertEquals(
            ReleaseListItemModel(
                id = "0bc23883-16c2-4f9b-b3f5-b3685e530435",
                name = "Keep The Beats!",
                formattedArtistCredits = "Girls Dead Monster",
            ),
            (searchResults[1] as ReleaseListItemModel),
        )
        Assert.assertEquals(
            EndOfList,
            searchResults.last(),
        )
    }
}
