package ly.david.musicsearch.shared.feature.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.RecordingsByEntityDao
import ly.david.musicsearch.domain.browse.usecase.ObserveBrowseEntityCount
import ly.david.musicsearch.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.shared.feature.stats.internal.StatsUiState
import ly.david.ui.common.screen.StatsScreen

internal class WorkStatsPresenter(
    private val screen: StatsScreen,
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val observeBrowseEntityCount: ObserveBrowseEntityCount,
    private val recordingsByEntityDao: RecordingsByEntityDao,
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
            observeBrowseEntityCount(
                entityId,
                MusicBrainzEntity.RECORDING,
            ),
            recordingsByEntityDao.getNumberOfRecordingsByEntity(entityId),
        ) { relationTypeCounts, browseRecordingCount, localRecordings ->
            Stats(
                totalRelations = relationTypeCounts.sumOf { it.count },
                relationTypeCounts = relationTypeCounts.toImmutableList(),
                recordingStats = RecordingStats(
                    totalRemote = browseRecordingCount?.remoteCount,
                    totalLocal = localRecordings,
                ),
            )
        }
            .distinctUntilChanged()
}
