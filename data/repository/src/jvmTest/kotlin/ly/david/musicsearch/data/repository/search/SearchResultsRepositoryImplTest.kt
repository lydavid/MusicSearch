package ly.david.musicsearch.data.repository.search

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeSearchApi
import ly.david.data.test.redReleaseMusicBrainzModel
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.MediumDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.database.dao.SearchResultDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.data.musicbrainz.api.SearchAreasResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchArtistsResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchReleasesResponse
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.helpers.TestReleaseRepository
import ly.david.musicsearch.data.repository.helpers.TestSearchResultsRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.listitem.Footer
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.listitem.SearchHeader
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class SearchResultsRepositoryImplTest : KoinTest, TestSearchResultsRepository, TestReleaseRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val searchResultDao: SearchResultDao by inject()
    override val areaDao: AreaDao by inject()
    override val artistDao: ArtistDao by inject()
    override val eventDao: EventDao by inject()
    override val instrumentDao: InstrumentDao by inject()
    override val labelDao: LabelDao by inject()
    override val mediumDao: MediumDao by inject()
    override val trackDao: TrackDao by inject()
    override val relationsMetadataDao: RelationsMetadataDao by inject()
    override val detailsMetadataDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()
    override val placeDao: PlaceDao by inject()
    override val recordingDao: RecordingDao by inject()
    override val releaseDao: ReleaseDao by inject()
    override val releaseReleaseGroupDao: ReleaseReleaseGroupDao by inject()
    override val releaseGroupDao: ReleaseGroupDao by inject()
    override val artistCreditDao: ArtistCreditDao by inject()
    override val seriesDao: SeriesDao by inject()
    override val workDao: WorkDao by inject()
    override val aliasDao: AliasDao by inject()

    @Test
    fun `empty network, no list items`() = runTest {
        val repository = createSearchResultsRepository(
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
    fun `results are ordered`() = runTest {
        val sut = createSearchResultsRepository(
            searchApi = object : FakeSearchApi() {
                override suspend fun queryArtists(
                    query: String,
                    limit: Int,
                    offset: Int,
                ): SearchArtistsResponse {
                    return SearchArtistsResponse(
                        count = 4,
                        artists = listOf(
                            ArtistMusicBrainzNetworkModel(
                                id = "89ad4ac3-39f7-470e-963a-56509c546377",
                                name = "Various Artists",
                            ),
                            ArtistMusicBrainzNetworkModel(
                                id = "b972f589-fb0e-474e-b64a-803b0364fa75",
                                name = "Wolfgang Amadeus Mozart",
                            ),
                            ArtistMusicBrainzNetworkModel(
                                id = "125ec42a-7229-4250-afc5-e057484327fe",
                                name = "[unknown]",
                            ),
                            ArtistMusicBrainzNetworkModel(
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
        val sut = createSearchResultsRepository(
            searchApi = object : FakeSearchApi() {
                override suspend fun queryAreas(
                    query: String,
                    limit: Int,
                    offset: Int,
                ): SearchAreasResponse {
                    return SearchAreasResponse(
                        count = 1,
                        areas = listOf(
                            AreaMusicBrainzNetworkModel(
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
                            ArtistMusicBrainzNetworkModel(
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

    @Test
    fun `release must include artist credits`() = runTest {
        val repository = createSearchResultsRepository(
            searchApi = object : FakeSearchApi() {
                override suspend fun queryReleases(
                    query: String,
                    limit: Int,
                    offset: Int,
                ): SearchReleasesResponse {
                    return SearchReleasesResponse(
                        count = 1,
                        releases = listOf(
                            ReleaseMusicBrainzNetworkModel(
                                id = "0bc23883-16c2-4f9b-b3f5-b3685e530435",
                                name = "Keep The Beats!",
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        artist = ArtistMusicBrainzNetworkModel(
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
                releaseGroup = ReleaseGroupMusicBrainzNetworkModel(
                    id = "a73cecde-0923-40ad-aad1-e8c24ba6c3d2",
                    name = "Red",
                    primaryType = "Album",
                ),
            ),
        ).lookupRelease(
            releaseId = redReleaseMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )

        val searchResultsRepository = createSearchResultsRepository(
            searchApi = object : FakeSearchApi() {
                override suspend fun queryAreas(
                    query: String,
                    limit: Int,
                    offset: Int,
                ): SearchAreasResponse {
                    return SearchAreasResponse(
                        count = 1,
                        areas = listOf(
                            AreaMusicBrainzNetworkModel(
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
