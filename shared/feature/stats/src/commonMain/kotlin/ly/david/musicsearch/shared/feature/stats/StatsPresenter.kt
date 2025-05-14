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
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.shared.domain.release.ReleasesListRepository
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsListRepository
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.toMusicBrainzEntity

internal class StatsPresenter(
    private val screen: StatsScreen,
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val browseRemoteMetadataRepository: BrowseRemoteMetadataRepository,
    private val artistDao: ArtistDao,
    private val eventDao: EventDao,
    private val labelDao: LabelDao,
    private val placeDao: PlaceDao,
    private val releasesListRepository: ReleasesListRepository,
    private val releaseGroupsListRepository: ReleaseGroupsListRepository,
    private val releaseGroupDao: ReleaseGroupDao,
    private val recordingDao: RecordingDao,
    private val workDao: WorkDao,
) : Presenter<StatsUiState> {

    @Composable
    override fun present(): StatsUiState {
        val relationTypeCounts
            by getCountOfEachRelationshipTypeUseCase(screen.id).collectAsState(listOf())
        val getLocalCountFlow: (
            entity: MusicBrainzEntity,
            entityId: String,
        ) -> Flow<Int> = { entity, entityId ->
            when (entity) {
                MusicBrainzEntity.ARTIST -> artistDao.observeCountOfArtistsByEntity(entityId)
                MusicBrainzEntity.EVENT -> eventDao.observeCountOfEventsByEntity(entityId)
                MusicBrainzEntity.LABEL -> labelDao.observeCountOfLabelsByEntity(entityId)
                MusicBrainzEntity.PLACE -> placeDao.observeCountOfPlacesByArea(entityId)
                MusicBrainzEntity.RECORDING -> recordingDao.observeCountOfRecordingsByEntity(entityId)
                MusicBrainzEntity.RELEASE -> releasesListRepository.observeCountOfReleases(
                    browseMethod = BrowseMethod.ByEntity(
                        entity = entity,
                        entityId = entityId,
                    ),
                ).map { it.toInt() }
                MusicBrainzEntity.RELEASE_GROUP -> releaseGroupsListRepository.observeCountOfReleaseGroups(
                    browseMethod = BrowseMethod.ByEntity(
                        entity = entity,
                        entityId = entityId,
                    ),
                ).map { it.toInt() }
                MusicBrainzEntity.WORK -> workDao.observeCountOfWorksByEntity(entityId)
                else -> flowOf(0)
            }
        }
        val getCountOfEachAlbumTypeFlow: (
            entity: MusicBrainzEntity,
            entityId: String,
        ) -> Flow<List<ReleaseGroupTypeCount>> =
            { entity, entityId ->
                if (entity == MusicBrainzEntity.RELEASE_GROUP) {
                    releaseGroupDao.getCountOfEachAlbumType(entityId)
                } else {
                    flowOf(listOf())
                }
            }
        val tabToStats = screen.tabs
            .filterNot { listOf(Tab.DETAILS, Tab.TRACKS, Tab.STATS).contains(it) }
            .associateWith { tab ->
                val entity = tab.toMusicBrainzEntity()
                observeEntityStats(
                    entityId = screen.id,
                    entity = entity,
                    localCountFlow = { entityId -> getLocalCountFlow(entity, entityId) },
                    countOfEachAlbumTypeFlow = { entityId -> getCountOfEachAlbumTypeFlow(entity, entityId) },
                ).collectAsState(EntityStats()).value
            }.toPersistentHashMap()

        val stats = Stats(
            totalRelations = relationTypeCounts.sumOf { it.count },
            relationTypeCounts = relationTypeCounts.toImmutableList(),
            tabToStats = tabToStats,
        )

        return StatsUiState(
            stats = stats,
            tabs = screen.tabs.toImmutableList(),
        )
    }

    private fun observeEntityStats(
        entityId: String,
        entity: MusicBrainzEntity,
        localCountFlow: (entityId: String) -> Flow<Int>,
        countOfEachAlbumTypeFlow: (entityId: String) -> Flow<List<ReleaseGroupTypeCount>>,
    ): Flow<EntityStats> {
        val browseRemoteMetadataFlow = browseRemoteMetadataRepository.observe(
            entityId = entityId,
            entity = entity,
        )
        return combine(
            browseRemoteMetadataFlow,
            localCountFlow(entityId),
            countOfEachAlbumTypeFlow(entityId),
        ) { browseRemoteMetadata, localCount, releaseGroupTypeCount ->
            EntityStats(
                totalRemote = browseRemoteMetadata?.remoteCount,
                totalLocal = localCount,
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
