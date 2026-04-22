package ly.david.musicsearch.shared.feature.stats

import com.slack.circuit.test.presenterTestOf
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
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
import ly.david.musicsearch.shared.domain.release.ObserveCountOfEachStatus
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.shared.domain.release.ReleaseStatusCount
import ly.david.musicsearch.shared.domain.releasegroup.ObserveCountOfEachAlbumType
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupPrimaryType
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupSecondaryType
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.ui.common.artist.artistTabs
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.topappbar.Tab
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.time.Instant

@RunWith(RobolectricTestRunner::class)
class StatsPresenterTest {

    private val lastUpdated = Instant.parse("2022-01-01T00:00:00Z")

    @Test
    fun `parameters are passed through`() = runTest {
        val presenter = StatsPresenter(
            screen = StatsScreen(
                browseMethod = BrowseMethod.ByEntity(
                    entityId = "1",
                    entityType = MusicBrainzEntityType.ARTIST,
                ),
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
                    query: String,
                    showReleaseStatuses: Set<ReleaseStatus>,
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
                    return flowOf(
                        listOf(
                            ReleaseGroupTypeCount(
                                primaryType = ReleaseGroupPrimaryType.Album,
                                secondaryTypes = persistentListOf(ReleaseGroupSecondaryType.Compilation),
                                count = 2,
                            ),
                        ),
                    )
                }
            },
            observeCountOfEachStatus = object : ObserveCountOfEachStatus {
                override fun invoke(browseMethod: BrowseMethod): Flow<List<ReleaseStatusCount>> {
                    return flowOf(
                        listOf(
                            ReleaseStatusCount(
                                status = ReleaseStatus.OFFICIAL,
                                count = 1,
                            ),
                            ReleaseStatusCount(
                                status = ReleaseStatus.UNKNOWN,
                                count = 2,
                            ),
                        ),
                    )
                }
            },
        )

        presenterTestOf({ presenter.present() }) {
            awaitItem().run {
                assertEquals(
                    StatsUiState(
                        stats = Stats(
                            tabToStats = persistentHashMapOf(
                                Tab.RELATIONSHIPS to EntityStats.Default(),
                                Tab.RECORDINGS to EntityStats.Default(),
                                Tab.RELEASE_GROUPS to EntityStats.Default(),
                                Tab.EVENTS to EntityStats.Default(),
                                Tab.WORKS to EntityStats.Default(),
                                Tab.RELEASES to EntityStats.Default(),
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
                                Tab.RELATIONSHIPS to EntityStats.Default(),
                                Tab.RECORDINGS to EntityStats.Default(
                                    totalRemote = 300,
                                    totalLocal = 200,
                                    totalVisited = 2,
                                    totalCollected = 1,
                                    lastUpdated = lastUpdated,
                                ),
                                Tab.RELEASE_GROUPS to EntityStats.ReleaseGroup(
                                    totalRemote = 300,
                                    totalLocal = 200,
                                    totalVisited = 2,
                                    totalCollected = 1,
                                    typeCounts = persistentListOf(
                                        ReleaseGroupTypeCount(
                                            primaryType = ReleaseGroupPrimaryType.Album,
                                            secondaryTypes = persistentListOf(ReleaseGroupSecondaryType.Compilation),
                                            count = 2,
                                        ),
                                    ),
                                    lastUpdated = lastUpdated,
                                ),
                                Tab.EVENTS to EntityStats.Default(
                                    totalRemote = 300,
                                    totalLocal = 200,
                                    totalVisited = 2,
                                    totalCollected = 1,
                                    lastUpdated = lastUpdated,
                                ),
                                Tab.WORKS to EntityStats.Default(
                                    totalRemote = 300,
                                    totalLocal = 200,
                                    totalVisited = 2,
                                    totalCollected = 1,
                                    lastUpdated = lastUpdated,
                                ),
                                Tab.RELEASES to EntityStats.Release(
                                    totalRemote = 300,
                                    totalLocal = 200,
                                    totalVisited = 2,
                                    totalCollected = 1,
                                    statusCounts = persistentListOf(
                                        ReleaseStatusCount(
                                            status = ReleaseStatus.OFFICIAL,
                                            count = 1,
                                        ),
                                        ReleaseStatusCount(
                                            status = ReleaseStatus.UNKNOWN,
                                            count = 2,
                                        ),
                                    ),
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
