package ly.david.musicsearch.data.repository.history

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeSearchApi
import ly.david.data.test.zutomayoArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.LookupHistoryDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.SearchResultDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.data.musicbrainz.api.SearchArtistsResponse
import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.LookupHistoryRepositoryImpl
import ly.david.musicsearch.data.repository.helpers.TestArtistRepository
import ly.david.musicsearch.data.repository.helpers.TestSearchResultsRepository
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.history.HistorySortOption
import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.history.LookupHistoryRepository
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.listitem.Footer
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.LookupHistoryListItemModel
import ly.david.musicsearch.shared.domain.listitem.SearchHeader
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class LookupHistoryRepositoryImplTest :
    KoinTest,
    TestSearchResultsRepository,
    TestArtistRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val searchResultDao: SearchResultDao by inject()
    override val areaDao: AreaDao by inject()
    override val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    override val artistDao: ArtistDao by inject()
    override val relationsMetadataDao: RelationsMetadataDao by inject()
    override val detailsMetadataDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()
    override val eventDao: EventDao by inject()
    override val instrumentDao: InstrumentDao by inject()
    override val labelDao: LabelDao by inject()
    override val placeDao: PlaceDao by inject()
    override val recordingDao: RecordingDao by inject()
    override val releaseDao: ReleaseDao by inject()
    override val releaseGroupDao: ReleaseGroupDao by inject()
    override val artistCreditDao: ArtistCreditDao by inject()
    override val seriesDao: SeriesDao by inject()
    override val workDao: WorkDao by inject()
    override val aliasDao: AliasDao by inject()
    override val coroutineDispatchers: CoroutineDispatchers by inject()
    private val lookupHistoryDao: LookupHistoryDao by inject()

    @Test
    fun `empty, upsert, filter`() = runTest {
        val lookupHistoryRepository: LookupHistoryRepository = LookupHistoryRepositoryImpl(
            lookupHistoryDao = lookupHistoryDao,
        )

        val emptyListItemModelList: List<ListItemModel> = lookupHistoryRepository.observeAllLookupHistory(
            query = "",
            sortOption = HistorySortOption.RECENTLY_VISITED,
        ).asSnapshot()
        Assert.assertEquals(
            listOf<ListItemModel>(),
            emptyListItemModelList,
        )

        val currentTime = Instant.parse("2025-02-09T13:37:02Z")

        lookupHistoryRepository.upsert(
            LookupHistory(
                mbid = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
                title = "欠けた心象、世のよすが",
                entity = MusicBrainzEntity.RELEASE_GROUP,
                lastAccessed = currentTime,
            ),
        )
        val listItemModelList: List<ListItemModel> = lookupHistoryRepository.observeAllLookupHistory(
            query = "",
            sortOption = HistorySortOption.RECENTLY_VISITED,
        ).asSnapshot()
        Assert.assertEquals(
            listOf(
                ListSeparator(
                    id = "1739108222",
                    text = "Sunday, February 9",
                ),
                LookupHistoryListItemModel(
                    title = "欠けた心象、世のよすが",
                    entity = MusicBrainzEntity.RELEASE_GROUP,
                    id = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
                    numberOfVisits = 1,
                    lastAccessed = currentTime,
                ),
            ),
            listItemModelList,
        )

        val filteredListItemModelList: List<ListItemModel> = lookupHistoryRepository.observeAllLookupHistory(
            query = "not found",
            sortOption = HistorySortOption.RECENTLY_VISITED,
        ).asSnapshot()
        Assert.assertEquals(
            listOf<ListItemModel>(),
            filteredListItemModelList,
        )
    }

    @Test
    fun `aliases are inserted after searching and are used in history`() = runTest {
        val aliases = listOf(
            AliasMusicBrainzNetworkModel(
                name = "ZUTOMAYO",
                isPrimary = true,
                locale = "en",
                typeId = "894afba6-2816-3c24-8072-eadb66bd04bc",
            ),
            AliasMusicBrainzNetworkModel(
                name = "ZTMY",
                isPrimary = null,
                locale = "en",
                typeId = "894afba6-2816-3c24-8072-eadb66bd04bc",
            ),
            AliasMusicBrainzNetworkModel(
                name = "계속 한밤중이면 좋을 텐데.",
                isPrimary = true,
                locale = "ko",
                typeId = "894afba6-2816-3c24-8072-eadb66bd04bc",
            ),
            AliasMusicBrainzNetworkModel(
                name = "ずとまよ",
                isPrimary = false,
                locale = "ja",
                typeId = "894afba6-2816-3c24-8072-eadb66bd04bc",
            ),
        )
        val searchResultsRepository = createSearchResultsRepository(
            searchApi = object : FakeSearchApi() {
                override suspend fun queryArtists(
                    query: String,
                    limit: Int,
                    offset: Int,
                ): SearchArtistsResponse {
                    return SearchArtistsResponse(
                        count = 1,
                        artists = listOf(
                            zutomayoArtistMusicBrainzNetworkModel.copy(
                                aliases = aliases,
                            ),
                        ),
                    )
                }
            },
        )

        searchResultsRepository.observeSearchResults(
            entity = MusicBrainzEntity.ARTIST,
            query = "zutomayo",
        ).asSnapshot().run {
            Assert.assertEquals(
                listOf(
                    SearchHeader(remoteCount = 1),
                    ArtistListItemModel(
                        id = "14d2a235-30e2-489f-b490-f9dc7d2c0861",
                        name = "ずっと真夜中でいいのに",
                        disambiguation = "Japanese pop band",
                        sortName = "Zutto Mayonaka de Iinoni.",
                        type = "Group",
                    ),
                    Footer(),
                ),
                this,
            )
        }

        val currentTime = Instant.parse("2024-06-05T19:42:20Z")
        createArtistRepository(
            artistMusicBrainzModel = zutomayoArtistMusicBrainzNetworkModel.copy(
                aliases = aliases,
            ),
        ).lookupArtist(
            artistId = zutomayoArtistMusicBrainzNetworkModel.id,
            forceRefresh = false,
            lastUpdated = currentTime,
        ).run {
            Assert.assertEquals(
                ArtistDetailsModel(
                    id = "14d2a235-30e2-489f-b490-f9dc7d2c0861",
                    name = "ずっと真夜中でいいのに",
                    disambiguation = "Japanese pop band",
                    type = "Group",
                    sortName = "Zutto Mayonaka de Iinoni.",
                    lastUpdated = currentTime,
                    aliases = listOf(
                        BasicAlias(
                            name = "ZUTOMAYO",
                            locale = "en",
                            isPrimary = true,
                        ),
                        BasicAlias(
                            name = "ZTMY",
                            locale = "en",
                            isPrimary = false,
                        ),
                        BasicAlias(
                            name = "계속 한밤중이면 좋을 텐데.",
                            locale = "ko",
                            isPrimary = true,
                        ),
                        BasicAlias(
                            name = "ずとまよ",
                            locale = "ja",
                            isPrimary = false,
                        ),
                    ),
                ),
                this,
            )
        }

        val lookupHistoryRepository: LookupHistoryRepository = LookupHistoryRepositoryImpl(
            lookupHistoryDao = lookupHistoryDao,
        )
        // Have to simulate visiting the artist page
        lookupHistoryRepository.upsert(
            LookupHistory(
                mbid = "14d2a235-30e2-489f-b490-f9dc7d2c0861",
                title = "ずっと真夜中でいいのに",
                entity = MusicBrainzEntity.ARTIST,
                searchHint = "Zutto Mayonaka de Iinoni.",
                lastAccessed = currentTime,
            ),
        )
        lookupHistoryRepository.observeAllLookupHistory(
            query = "zutomayo",
            sortOption = HistorySortOption.RECENTLY_VISITED,
        ).asSnapshot().run {
            Assert.assertEquals(
                listOf(
                    ListSeparator(
                        id = "1717616540",
                        text = "Wednesday, June 5",
                    ),
                    LookupHistoryListItemModel(
                        title = "ずっと真夜中でいいのに",
                        entity = MusicBrainzEntity.ARTIST,
                        id = "14d2a235-30e2-489f-b490-f9dc7d2c0861",
                        numberOfVisits = 1,
                        lastAccessed = currentTime,
                    ),
                ),
                this,
            )
        }
        lookupHistoryRepository.observeAllLookupHistory(
            query = "zutomayoo",
            sortOption = HistorySortOption.RECENTLY_VISITED,
        ).asSnapshot().run {
            Assert.assertEquals(
                emptyList<ListItemModel>(),
                this,
            )
        }
    }
}
