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
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.list.EntitiesListRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsListRepository
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.toMusicBrainzEntity

internal class StatsPresenter(
    private val screen: StatsScreen,
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val browseRemoteMetadataRepository: BrowseRemoteMetadataRepository,
    private val entitiesListRepository: EntitiesListRepository,
    private val releaseGroupsListRepository: ReleaseGroupsListRepository,
) : Presenter<StatsUiState> {

    @Composable
    override fun present(): StatsUiState {
        val byEntity = BrowseMethod.ByEntity(
            entityId = screen.byEntityId,
            entity = screen.byEntity,
        )
        val relationTypeCounts
            by getCountOfEachRelationshipTypeUseCase(byEntity.entityId).collectAsState(listOf())

        val tabToStats = screen.tabs
            .filterNot { setOf(Tab.DETAILS, Tab.TRACKS, Tab.STATS).contains(it) }
            .associateWith { tab ->
                val browseEntity = tab.toMusicBrainzEntity() ?: return@associateWith EntityStats()
                observeEntityStats(
                    byEntity = byEntity,
                    entity = browseEntity,
                    localCountFlow = {
                        entitiesListRepository.observeLocalCount(
                            browseEntity = browseEntity,
                            browseMethod = byEntity,
                        )
                    },
                    visitedCountFlow = {
                        entitiesListRepository.observeVisitedCount(
                            browseEntity = browseEntity,
                            browseMethod = byEntity,
                        )
                    },
                    countOfEachAlbumTypeFlow = { entityId ->
                        if (browseEntity == MusicBrainzEntity.RELEASE_GROUP) {
                            observeCountOfEachAlbumType(
                                entityId = entityId,
                                isCollection = byEntity.entity == MusicBrainzEntity.COLLECTION,
                            )
                        } else {
                            flowOf(listOf())
                        }
                    },
                ).collectAsState(EntityStats()).value
            }.toPersistentHashMap()

        val stats = Stats(
            totalRelations = relationTypeCounts.sumOf { it.count },
            relationTypeCounts = relationTypeCounts.toImmutableList(),
            tabToStats = tabToStats,
        )

        return StatsUiState(
            stats = stats,
            tabs = screen.tabs,
        )
    }

    private fun observeCountOfEachAlbumType(
        entityId: String,
        isCollection: Boolean,
    ): Flow<List<ReleaseGroupTypeCount>> {
        return releaseGroupsListRepository.observeCountOfEachAlbumType(
            entityId = entityId,
            isCollection = isCollection,
        )
    }

    private fun observeEntityStats(
        entity: MusicBrainzEntity,
        byEntity: BrowseMethod.ByEntity,
        localCountFlow: () -> Flow<Int>,
        visitedCountFlow: () -> Flow<Int?>,
        countOfEachAlbumTypeFlow: (entityId: String) -> Flow<List<ReleaseGroupTypeCount>>,
    ): Flow<EntityStats> {
        val browseRemoteMetadataFlow = browseRemoteMetadataRepository.observe(
            entityId = byEntity.entityId,
            entity = entity,
        )
        return combine(
            browseRemoteMetadataFlow,
            localCountFlow(),
            visitedCountFlow(),
            countOfEachAlbumTypeFlow(byEntity.entityId),
        ) { browseRemoteMetadata, localCount, visitedCount, releaseGroupTypeCount ->
            EntityStats(
                // we distinguish between null and 0
                totalRemote = browseRemoteMetadata?.remoteCount?.let { remoteCount ->
                    // after adding to a remote collection, the local count may be higher than the remote count
                    maxOf(localCount, remoteCount)
                },
                totalLocal = localCount,
                totalVisited = visitedCount,
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
}
