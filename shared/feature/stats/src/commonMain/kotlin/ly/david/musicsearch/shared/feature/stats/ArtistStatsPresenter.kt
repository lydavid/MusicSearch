package ly.david.musicsearch.shared.feature.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.data.database.dao.ArtistReleaseDao
import ly.david.musicsearch.data.database.dao.ArtistReleaseGroupDao
import ly.david.musicsearch.data.database.dao.EventsByEntityDao
import ly.david.musicsearch.data.database.dao.RecordingsByEntityDao
import ly.david.musicsearch.domain.browse.usecase.ObserveBrowseEntityCount
import ly.david.musicsearch.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.shared.feature.stats.internal.StatsUiState
import ly.david.ui.common.screen.StatsScreen

internal class ArtistStatsPresenter(
    private val screen: StatsScreen,
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val observeBrowseEntityCount: ObserveBrowseEntityCount,
    private val eventsByEntityDao: EventsByEntityDao,
    private val recordingsByEntityDao: RecordingsByEntityDao,
    private val artistReleaseGroupDao: ArtistReleaseGroupDao,
    private val artistReleaseDao: ArtistReleaseDao,
) : Presenter<StatsUiState> {

    @Composable
    override fun present(): StatsUiState {
        val stats = getStats(screen.id).collectAsState(Stats())

        return StatsUiState(
            stats = stats.value,
            tabs = screen.tabs.toImmutableList(),
        )
    }

    private fun getStats(entityId: String): Flow<Stats> =
        combine(
            getCountOfEachRelationshipTypeUseCase(entityId),
            releaseGroupStats(entityId),
            releaseStats(entityId),
            recordingsStats(entityId),
            eventsStats(entityId),
        ) {
                relationTypeCounts,
                releaseGroupStats,
                releaseStats,
                recordingStats,
                eventStats,
            ->

            Stats(
                totalRelations = relationTypeCounts.sumOf { it.count },
                relationTypeCounts = relationTypeCounts.toImmutableList(),
                releaseGroupStats = releaseGroupStats,
                releaseStats = releaseStats,
                recordingStats = recordingStats,
                eventStats = eventStats,
            )
        }
            .distinctUntilChanged()

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
            artistReleaseGroupDao.getNumberOfReleaseGroupsByArtist(entityId),
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
            recordingsByEntityDao.getNumberOfRecordingsByEntity(entityId),
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
            eventsByEntityDao.getNumberOfEventsByEntity(entityId),
        ) { browseReleaseCount, localReleases ->
            EventStats(
                totalRemote = browseReleaseCount?.remoteCount,
                totalLocal = localReleases,
            )
        }
}
