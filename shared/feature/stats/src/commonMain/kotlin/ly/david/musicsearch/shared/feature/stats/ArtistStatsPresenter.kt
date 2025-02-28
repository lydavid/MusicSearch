package ly.david.musicsearch.shared.feature.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ly.david.musicsearch.data.database.dao.ArtistReleaseDao
import ly.david.musicsearch.data.database.dao.ArtistReleaseGroupDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.RecordingsByEntityDao
import ly.david.musicsearch.data.database.dao.WorksByEntityDao
import ly.david.musicsearch.shared.domain.browse.usecase.ObserveBrowseEntityCount
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.shared.feature.stats.internal.StatsUiState
import ly.david.musicsearch.ui.common.screen.StatsScreen

internal class ArtistStatsPresenter(
    private val screen: StatsScreen,
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val observeBrowseEntityCount: ObserveBrowseEntityCount,
    private val eventDao: EventDao,
    private val recordingsByEntityDao: RecordingsByEntityDao,
    private val worksByEntityDao: WorksByEntityDao,
    private val artistReleaseGroupDao: ArtistReleaseGroupDao,
    private val artistReleaseDao: ArtistReleaseDao,
) : Presenter<StatsUiState> {

    @Composable
    override fun present(): StatsUiState {
        val relationTypeCounts
            by getCountOfEachRelationshipTypeUseCase(screen.id).collectAsState(listOf())
        val releaseStats by releaseStats(screen.id).collectAsState(ReleaseStats())
        val releaseGroupStats by releaseGroupStats(screen.id).collectAsState(ReleaseGroupStats())
        val recordingStats by recordingsStats(screen.id).collectAsState(RecordingStats())
        val eventStats by eventsStats(screen.id).collectAsState(EventStats())
        val workStats by worksStats(screen.id).collectAsState(WorkStats())
        val stats = Stats(
            totalRelations = relationTypeCounts.sumOf { it.count },
            relationTypeCounts = relationTypeCounts.toImmutableList(),
            releaseGroupStats = releaseGroupStats,
            releaseStats = releaseStats,
            recordingStats = recordingStats,
            eventStats = eventStats,
            workStats = workStats,
        )

        return StatsUiState(
            stats = stats,
            tabs = screen.tabs.toImmutableList(),
        )
    }

    private fun releaseStats(entityId: String): Flow<ReleaseStats> =
        combine(
            observeBrowseEntityCount(
                entityId,
                MusicBrainzEntity.RELEASE,
            ),
            artistReleaseDao.getNumberOfReleasesByArtist(entityId),
        ) { browseReleaseCount, localReleases ->
            ReleaseStats(
                totalRemote = browseReleaseCount?.remoteCount,
                totalLocal = localReleases,
            )
        }

    private fun releaseGroupStats(entityId: String): Flow<ReleaseGroupStats> =
        combine(
            observeBrowseEntityCount(
                entityId,
                MusicBrainzEntity.RELEASE_GROUP,
            ),
            artistReleaseGroupDao.observeCountOfReleaseGroupsByArtist(entityId),
            artistReleaseGroupDao.getCountOfEachAlbumType(entityId),
        ) { browseReleaseGroupCount, localReleaseGroups, releaseGroupTypeCount ->
            ReleaseGroupStats(
                totalRemote = browseReleaseGroupCount?.remoteCount,
                totalLocal = localReleaseGroups,
                releaseGroupTypeCounts = releaseGroupTypeCount.map {
                    ReleaseGroupTypeCount(
                        primaryType = it.primaryType,
                        secondaryTypes = it.secondaryTypes,
                        count = it.count,
                    )
                }.toImmutableList(),
            )
        }

    private fun recordingsStats(entityId: String): Flow<RecordingStats> =
        combine(
            observeBrowseEntityCount(
                entityId,
                MusicBrainzEntity.RECORDING,
            ),
            recordingsByEntityDao.observeCountOfRecordingsByEntity(entityId),
        ) { browseRecordingCount, localRecordings ->
            RecordingStats(
                totalRemote = browseRecordingCount?.remoteCount,
                totalLocal = localRecordings,
            )
        }

    private fun eventsStats(entityId: String): Flow<EventStats> =
        combine(
            observeBrowseEntityCount(
                entityId,
                MusicBrainzEntity.EVENT,
            ),
            eventDao.observeCountOfEventsByEntity(entityId),
        ) { browseReleaseCount, localReleases ->
            EventStats(
                totalRemote = browseReleaseCount?.remoteCount,
                totalLocal = localReleases,
            )
        }

    private fun worksStats(entityId: String): Flow<WorkStats> =
        combine(
            observeBrowseEntityCount(
                entityId,
                MusicBrainzEntity.WORK,
            ),
            worksByEntityDao.observeCountOfWorksByEntity(entityId),
        ) { browseReleaseCount, localReleases ->
            WorkStats(
                totalRemote = browseReleaseCount?.remoteCount,
                totalLocal = localReleases,
            )
        }
}
