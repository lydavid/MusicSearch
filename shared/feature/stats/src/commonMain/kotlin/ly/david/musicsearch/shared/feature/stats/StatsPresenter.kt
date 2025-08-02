package ly.david.musicsearch.shared.feature.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentHashMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadata
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.list.ObserveCollectedCount
import ly.david.musicsearch.shared.domain.list.ObserveLocalCount
import ly.david.musicsearch.shared.domain.list.ObserveVisitedCount
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.RelationStats
import ly.david.musicsearch.shared.domain.relation.usecase.ObserveRelationStatsUseCase
import ly.david.musicsearch.shared.domain.releasegroup.ObserveCountOfEachAlbumType
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.toMusicBrainzEntity

internal class StatsPresenter(
    private val screen: StatsScreen,
    private val observeRelationStatsUseCase: ObserveRelationStatsUseCase,
    private val browseRemoteMetadataRepository: BrowseRemoteMetadataRepository,
    private val observeLocalCount: ObserveLocalCount,
    private val observeVisitedCount: ObserveVisitedCount,
    private val observeCollectedCount: ObserveCollectedCount,
    private val observeCountOfEachAlbumType: ObserveCountOfEachAlbumType,
) : Presenter<StatsUiState> {

    @Composable
    override fun present(): StatsUiState {
        val byEntityId = screen.byEntityId
        val byEntity = screen.byEntity
        val browseMethod = if (byEntityId == null || byEntity == null) {
            BrowseMethod.All
        } else {
            BrowseMethod.ByEntity(
                entityId = byEntityId,
                entity = byEntity,
            )
        }
        val relationStats by observeRelationStatsUseCase(browseMethod).collectAsState(RelationStats())

        val tabToStats = screen.tabs
            .filterNot { setOf(Tab.DETAILS, Tab.TRACKS, Tab.STATS).contains(it) }
            .associateWith { tab ->
                val browseEntity = tab.toMusicBrainzEntity() ?: return@associateWith EntityStats()
                observeEntityStats(
                    browseEntity = browseEntity,
                    browseMethod = browseMethod,
                    countOfEachAlbumTypeFlow = {
                        if (browseEntity == MusicBrainzEntity.RELEASE_GROUP) {
                            observeCountOfEachAlbumType(
                                browseMethod = browseMethod,
                            )
                        } else {
                            flowOf(listOf())
                        }
                    },
                ).collectAsState(EntityStats()).value
            }.toPersistentHashMap()

        val stats = Stats(
            relationStats = relationStats,
            tabToStats = tabToStats,
        )

        return StatsUiState(
            stats = stats,
            tabs = screen.tabs,
        )
    }

    private fun observeEntityStats(
        browseEntity: MusicBrainzEntity,
        browseMethod: BrowseMethod,
        countOfEachAlbumTypeFlow: () -> Flow<List<ReleaseGroupTypeCount>>,
    ): Flow<EntityStats> {
        val browseRemoteMetadataFlow: Flow<BrowseRemoteMetadata?> = when (browseMethod) {
            BrowseMethod.All -> {
                flowOf(null)
            }
            is BrowseMethod.ByEntity -> {
                browseRemoteMetadataRepository.observe(
                    entityId = browseMethod.entityId,
                    entity = browseEntity,
                )
            }
        }
        val localCountFlow = observeLocalCount(
            browseEntity = browseEntity,
            browseMethod = browseMethod,
        )
        val visitedCountFlow = observeVisitedCount(
            browseEntity = browseEntity,
            browseMethod = browseMethod,
        )
        val collectedCountFlow = observeCollectedCount(
            browseEntity = browseEntity,
            browseMethod = browseMethod,
        )
        return combine(
            browseRemoteMetadataFlow,
            localCountFlow,
            visitedCountFlow,
            collectedCountFlow,
            countOfEachAlbumTypeFlow(),
        ) { browseRemoteMetadata, localCount, visitedCount, collectedCount, releaseGroupTypeCount ->
            EntityStats(
                totalRemote = getTotalRemote(
                    browseRemoteMetadata = browseRemoteMetadata,
                    localCount = localCount,
                ),
                totalLocal = localCount,
                totalVisited = visitedCount,
                totalCollected = collectedCount,
                releaseGroupTypeCounts = releaseGroupTypeCount.map {
                    ReleaseGroupTypeCount(
                        primaryType = it.primaryType,
                        secondaryTypes = it.secondaryTypes,
                        count = it.count,
                    )
                }.toImmutableList(),
                lastUpdated = browseRemoteMetadata?.lastUpdated,
            )
        }
    }

    private fun getTotalRemote(
        browseRemoteMetadata: BrowseRemoteMetadata?,
        localCount: Int,
    ): Int? {
        val remoteCount = if (screen.isRemote) {
            browseRemoteMetadata?.remoteCount
        } else {
            localCount
        }
        // we distinguish between null and 0 for remote
        return remoteCount?.let { remoteCount ->
            // after adding to a remote collection, the local count may be higher than the remote count
            maxOf(localCount, remoteCount)
        }
    }
}
