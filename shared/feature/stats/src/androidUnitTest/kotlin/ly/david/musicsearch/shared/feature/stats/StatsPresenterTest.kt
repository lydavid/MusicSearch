package ly.david.musicsearch.shared.feature.stats

import com.slack.circuit.test.presenterTestOf
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadata
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.list.ObserveCollectedCount
import ly.david.musicsearch.shared.domain.list.ObserveLocalCount
import ly.david.musicsearch.shared.domain.list.ObserveVisitedCount
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationStats
import ly.david.musicsearch.shared.domain.relation.RelationTypeCount
import ly.david.musicsearch.shared.domain.relation.usecase.ObserveRelationStatsUseCase
import ly.david.musicsearch.shared.domain.releasegroup.ObserveCountOfEachAlbumType
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.ui.common.artist.artistTabs
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.topappbar.Tab
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StatsPresenterTest {

    private val lastUpdated = Instant.parse("2022-01-01T00:00:00Z")

    @Test
    fun `parameters are passed through`() = runTest {
        val presenter = StatsPresenter(
            screen = StatsScreen(
                byEntityId = "1",
                byEntity = MusicBrainzEntityType.ARTIST,
                tabs = artistTabs,
            ),
            observeRelationStatsUseCase = object : ObserveRelationStatsUseCase {
                override fun invoke(browseMethod: BrowseMethod): Flow<RelationStats> {
                    return flowOf(
                        RelationStats(
                            relationTypeCounts = persistentListOf(
                                RelationTypeCount(
                                    linkedEntity = MusicBrainzEntityType.AREA,
                                    count = 1,
                                ),
                                RelationTypeCount(
                                    linkedEntity = MusicBrainzEntityType.ARTIST,
                                    count = 2,
                                ),
                                RelationTypeCount(
                                    linkedEntity = MusicBrainzEntityType.EVENT,
                                    count = 3,
                                ),
                            ),
                            lastUpdated = lastUpdated,
                        ),
                    )
                }
            },
            browseRemoteMetadataRepository = object : BrowseRemoteMetadataRepository {
                override fun observe(
                    entityId: String,
                    entity: MusicBrainzEntityType,
                ): Flow<BrowseRemoteMetadata?> {
                    return flowOf(
                        BrowseRemoteMetadata(
                            remoteCount = 300,
                            lastUpdated = lastUpdated,
                        ),
                    )
                }

                override fun get(
                    entityId: String,
                    entity: MusicBrainzEntityType,
                ): BrowseRemoteMetadata? {
                    error("unused")
                }
            },
            observeLocalCount = object : ObserveLocalCount {
                override fun invoke(
                    browseEntity: MusicBrainzEntityType,
                    browseMethod: BrowseMethod?,
                ): Flow<Int> {
                    return flowOf(200)
                }
            },
            observeVisitedCount = object : ObserveVisitedCount {
                override fun invoke(
                    browseEntity: MusicBrainzEntityType,
                    browseMethod: BrowseMethod?,
                ): Flow<Int> {
                    return flowOf(2)
                }
            },
            observeCollectedCount = object : ObserveCollectedCount {
                override fun invoke(
                    browseEntity: MusicBrainzEntityType,
                    browseMethod: BrowseMethod?,
                ): Flow<Int> {
                    return flowOf(1)
                }
            },
            observeCountOfEachAlbumType = object : ObserveCountOfEachAlbumType {
                override operator fun invoke(
                    browseMethod: BrowseMethod,
                ): Flow<List<ReleaseGroupTypeCount>> {
                    return flowOf(listOf())
                }
            },
        )

        presenterTestOf({ presenter.present() }) {
            awaitItem().run {
                assertEquals(
                    StatsUiState(
                        stats = Stats(
                            tabToStats = persistentHashMapOf(
                                Tab.RELATIONSHIPS to EntityStats(),
                                Tab.RECORDINGS to EntityStats(),
                                Tab.RELEASE_GROUPS to EntityStats(),
                                Tab.EVENTS to EntityStats(),
                                Tab.WORKS to EntityStats(),
                                Tab.RELEASES to EntityStats(),
                            ),
                        ),
                        tabs = artistTabs,
                    ),
                    this,
                )
            }
            awaitItem().run {
                assertEquals(
                    StatsUiState(
                        stats = Stats(
                            relationStats = RelationStats(
                                relationTypeCounts = persistentListOf(
                                    RelationTypeCount(
                                        linkedEntity = MusicBrainzEntityType.AREA,
                                        count = 1,
                                    ),
                                    RelationTypeCount(
                                        linkedEntity = MusicBrainzEntityType.ARTIST,
                                        count = 2,
                                    ),
                                    RelationTypeCount(
                                        linkedEntity = MusicBrainzEntityType.EVENT,
                                        count = 3,
                                    ),
                                ),
                                lastUpdated = lastUpdated,
                            ),
                            tabToStats = persistentHashMapOf(
                                Tab.RELATIONSHIPS to EntityStats(),
                                Tab.RECORDINGS to EntityStats(
                                    totalRemote = 300,
                                    totalLocal = 200,
                                    totalVisited = 2,
                                    totalCollected = 1,
                                    lastUpdated = lastUpdated,
                                ),
                                Tab.RELEASE_GROUPS to EntityStats(
                                    totalRemote = 300,
                                    totalLocal = 200,
                                    totalVisited = 2,
                                    totalCollected = 1,
                                    lastUpdated = lastUpdated,
                                ),
                                Tab.EVENTS to EntityStats(
                                    totalRemote = 300,
                                    totalLocal = 200,
                                    totalVisited = 2,
                                    totalCollected = 1,
                                    lastUpdated = lastUpdated,
                                ),
                                Tab.WORKS to EntityStats(
                                    totalRemote = 300,
                                    totalLocal = 200,
                                    totalVisited = 2,
                                    totalCollected = 1,
                                    lastUpdated = lastUpdated,
                                ),
                                Tab.RELEASES to EntityStats(
                                    totalRemote = 300,
                                    totalLocal = 200,
                                    totalVisited = 2,
                                    totalCollected = 1,
                                    lastUpdated = lastUpdated,
                                ),
                            ),
                        ),
                        tabs = artistTabs,
                    ),
                    this,
                )
            }
        }
    }
}
