package ly.david.musicsearch.data.repository.search

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeSearchApi
import ly.david.data.test.redReleaseMusicBrainzModel
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.MediumDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.database.dao.SearchResultDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.database.dao.TrackDao
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
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.repository.helpers.TestReleaseRepository
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.listitem.Footer
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.listitem.SearchHeader
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.search.results.SearchResultsRepository
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class SearchResultsRepositoryImplTest : KoinTest, TestReleaseRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val searchResultDao: SearchResultDao by inject()
    override val areaDao: AreaDao by inject()
    private val artistDao: ArtistDao by inject()
    private val eventDao: EventDao by inject()
    private val instrumentDao: InstrumentDao by inject()
    override val labelDao: LabelDao by inject()
    override val mediumDao: MediumDao by inject()
    override val trackDao: TrackDao by inject()
    override val entityHasRelationsDao: EntityHasRelationsDao by inject()
    override val visitedDao: VisitedDao by inject()
    override val relationDao: RelationDao by inject()
    private val placeDao: PlaceDao by inject()
    private val recordingDao: RecordingDao by inject()
    override val releaseDao: ReleaseDao by inject()
    override val releaseReleaseGroupDao: ReleaseReleaseGroupDao by inject()
    override val releaseGroupDao: ReleaseGroupDao by inject()
    override val artistCreditDao: ArtistCreditDao by inject()
    private val seriesDao: SeriesDao by inject()
    private val workDao: WorkDao by inject()

    private fun createRepository(
        searchApi: SearchApi,
    ): SearchResultsRepository {
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
        val repository = createRepository(
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
        val repository = createRepository(
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
            listOf(
                SearchHeader(
                    remoteCount = 1,
                ),
                ArtistListItemModel(
                    id = "1",
                    name = "Various Artists",
                ),
                Footer(),
            ),
            searchResults,
        )
    }

    @Test
    fun `results are ordered`() = runTest {
        val sut = createRepository(
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
            listOf(
                SearchHeader(remoteCount = 4),
                ArtistListItemModel(
                    id = "89ad4ac3-39f7-470e-963a-56509c546377",
                    name = "Various Artists",
                ),
                ArtistListItemModel(
                    id = "b972f589-fb0e-474e-b64a-803b0364fa75",
                    name = "Wolfgang Amadeus Mozart",
                ),
                ArtistListItemModel(
                    id = "125ec42a-7229-4250-afc5-e057484327fe",
                    name = "[unknown]",
                ),
                ArtistListItemModel(
                    id = "7364dea6-ca9a-48e3-be01-b44ad0d19897",
                    name = "a-ha",
                ),
                Footer(),
            ),
            searchResults,
        )
    }

    @Test
    fun `changing query will not have old results show up`() = runTest {
        val sut = createRepository(
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
            listOf(
                SearchHeader(remoteCount = 1),
                ArtistListItemModel(
                    id = "89ad4ac3-39f7-470e-963a-56509c546377",
                    name = "Various Artists",
                ),
                Footer(),
            ),
            searchResults,
        )

        flow = sut.observeSearchResults(
            entity = MusicBrainzEntity.AREA,
            query = "a",
        )
        searchResults = flow.asSnapshot()
        Assert.assertEquals(
            listOf(
                SearchHeader(remoteCount = 1),
                AreaListItemModel(
                    id = "f42c1e2a-b7db-4acf-9742-1889b9c6d530",
                    name = "A Coruña",
                ),
                Footer(),
            ),
            searchResults,
        )
    }

    // This shouldn't be a problem as long as our LazyColumn is not keyed
    @Test
    fun `duplicates are shown`() = runTest {
        val repository = createRepository(
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
            listOf(
                SearchHeader(remoteCount = 2),
                EventListItemModel(
                    id = "1bc74971-d5c8-4a21-b761-c24e74efb9b4",
                    name = "Lollapalooza 2024",
                ),
                EventListItemModel(
                    id = "1bc74971-d5c8-4a21-b761-c24e74efb9b4",
                    name = "Lollapalooza 2024",
                ),
                Footer(),
            ),
            searchResults,
        )
    }

    @Test
    fun `release must include artist credits`() = runTest {
        val repository = createRepository(
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
            listOf(
                SearchHeader(remoteCount = 1),
                ReleaseListItemModel(
                    id = "0bc23883-16c2-4f9b-b3f5-b3685e530435",
                    name = "Keep The Beats!",
                    formattedArtistCredits = "Girls Dead Monster",
                ),
                Footer(),
            ),
            searchResults,
        )
    }

    // release events used to insert the country area with null type
    // then when we search for the country, we would not override its type, until we click into it
    @Test
    fun `inserting country from release event before searching for it will still show type`() = runTest {
        createReleaseRepository(
            redReleaseMusicBrainzModel.copy(
                releaseGroup = ReleaseGroupMusicBrainzModel(
                    id = "a73cecde-0923-40ad-aad1-e8c24ba6c3d2",
                    name = "Red",
                    primaryType = "Album",
                ),
            ),
        ).lookupRelease(
            releaseId = redReleaseMusicBrainzModel.id,
            forceRefresh = false,
        )

        val searchResultsRepository = createRepository(
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
                                id = "c6500277-9a3d-349b-bf30-41afdbf42add",
                                name = "Italy",
                                sortName = "Italy",
                                countryCodes = listOf("IT"),
                                type = "Country",
                                typeId = "06dd0ae4-8c74-30bb-b43d-95dcedf961de",
                            ),
                        ),
                    )
                }
            },
        )
        searchResultsRepository.observeSearchResults(
            entity = MusicBrainzEntity.AREA,
            query = "iso1:IT",
        ).asSnapshot().run {
            Assert.assertEquals(
                listOf(
                    SearchHeader(remoteCount = 1),
                    AreaListItemModel(
                        id = "c6500277-9a3d-349b-bf30-41afdbf42add",
                        name = "Italy",
                        sortName = "Italy",
                        countryCodes = listOf("IT"),
                        type = "Country",
                        visited = false,
                    ),
                    Footer(),
                ),
                this,
            )
        }
    }
}
