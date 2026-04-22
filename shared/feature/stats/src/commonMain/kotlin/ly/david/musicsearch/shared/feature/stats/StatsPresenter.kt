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
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadata
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.list.ObserveCollectedCount
import ly.david.musicsearch.shared.domain.list.ObserveLocalCount
import ly.david.musicsearch.shared.domain.list.ObserveVisitedCount
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationStats
import ly.david.musicsearch.shared.domain.relation.usecase.ObserveRelationStatsUseCase
import ly.david.musicsearch.shared.domain.release.ObserveCountOfEachStatus
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.shared.domain.releasegroup.ObserveCountOfEachAlbumType
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.toMusicBrainzEntityType

internal class StatsPresenter(
    private val screen: StatsScreen,
    private val observeRelationStatsUseCase: ObserveRelationStatsUseCase,
    private val browseRemoteMetadataRepository: BrowseRemoteMetadataRepository,
    private val observeLocalCount: ObserveLocalCount,
    private val observeVisitedCount: ObserveVisitedCount,
    private val observeCollectedCount: ObserveCollectedCount,
    private val observeCountOfEachAlbumType: ObserveCountOfEachAlbumType,
    private val observeCountOfEachStatus: ObserveCountOfEachStatus,
) : Presenter<StatsUiState> {

    @Composable
    override fun present(): StatsUiState {
        val browseMethod = screen.browseMethod
        val relationStats by observeRelationStatsUseCase(browseMethod).collectAsState(RelationStats())

        val tabToStats = screen.tabs
            .filterNot { setOf(Tab.DETAILS, Tab.TRACKS).contains(it) }
            .associateWith { tab ->
                val browseEntity = tab.toMusicBrainzEntityType() ?: return@associateWith EntityStats.Default()
                observeEntityStats(
                    browseEntityType = browseEntity,
                    browseMethod = browseMethod,
                ).collectAsState(EntityStats.Default()).value
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

    @Suppress("LongMethod", "DestructuringDeclarationWithTooManyEntries")
    private fun observeEntityStats(
        browseEntityType: MusicBrainzEntityType,
        browseMethod: BrowseMethod,
    ): Flow<EntityStats> {
        val browseRemoteMetadataFlow = observeBrowseRemoteMetadata(browseEntityType, browseMethod)
        val localCountFlow = observeLocalCount(
            browseEntity = browseEntityType,
            browseMethod = browseMethod,
            query = "",
            showReleaseStatuses = ReleaseStatus.entries.toSet(),
        )
        val visitedCountFlow = observeVisitedCount(browseEntity = browseEntityType, browseMethod = browseMethod)
        val collectedCountFlow = observeCollectedCount(browseEntity = browseEntityType, browseMethod = browseMethod)

        val baseFlow = combine(
            browseRemoteMetadataFlow,
            localCountFlow,
            visitedCountFlow,
            collectedCountFlow,
        ) { browseRemoteMetadata, localCount, visitedCount, collectedCount ->
            BaseStats(browseRemoteMetadata, localCount, visitedCount, collectedCount)
        }

        return when (browseEntityType) {
            MusicBrainzEntityType.RELEASE_GROUP -> {
                combine(
                    baseFlow,
                    observeCountOfEachAlbumType(browseMethod),
                ) { (metadata, local, visited, collected), typeCounts ->
                    EntityStats.ReleaseGroup(
                        totalRemote = getTotalRemote(metadata, local),
                        totalLocal = local,
                        totalVisited = visited,
                        totalCollected = collected,
                        lastUpdated = metadata?.lastUpdated,
                        typeCounts = typeCounts.map {
                            ReleaseGroupTypeCount(it.primaryType, it.secondaryTypes, it.count)
                        }.toImmutableList(),
                    )
                }
            }

            MusicBrainzEntityType.RELEASE -> {
                combine(
                    baseFlow,
                    observeCountOfEachStatus(browseMethod),
                ) { (metadata, local, visited, collected), statusCounts ->
                    EntityStats.Release(
                        totalRemote = getTotalRemote(metadata, local),
                        totalLocal = local,
                        totalVisited = visited,
                        totalCollected = collected,
                        lastUpdated = metadata?.lastUpdated,
                        statusCounts = statusCounts
                            .sortedBy { it.status.order }
                            .toImmutableList(),
                    )
                }
            }

            else -> {
                baseFlow.map { (metadata, local, visited, collected) ->
                    EntityStats.Default(
                        totalRemote = getTotalRemote(metadata, local),
                        totalLocal = local,
                        totalVisited = visited,
                        totalCollected = collected,
                        lastUpdated = metadata?.lastUpdated,
                    )
                }
            }
        }
    }

    private fun observeBrowseRemoteMetadata(
        browseEntityType: MusicBrainzEntityType,
        browseMethod: BrowseMethod,
    ): Flow<BrowseRemoteMetadata?> = when (browseMethod) {
        BrowseMethod.All -> flowOf(null)
        is BrowseMethod.ByEntity -> browseRemoteMetadataRepository.observe(
            entityId = browseMethod.entityId,
            browseEntityType = browseEntityType,
        )
    }

    private data class BaseStats(
        val browseRemoteMetadata: BrowseRemoteMetadata?,
        val localCount: Int,
        val visitedCount: Int,
        val collectedCount: Int,
    )

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
