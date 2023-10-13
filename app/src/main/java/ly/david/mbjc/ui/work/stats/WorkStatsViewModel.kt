package ly.david.mbjc.ui.work.stats

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.RecordingWorkDao
import ly.david.musicsearch.domain.browse.GetBrowseEntityCountFlowUseCase
import ly.david.musicsearch.domain.relation.GetCountOfEachRelationshipTypeUseCase
import ly.david.ui.stats.RecordingStats
import ly.david.ui.stats.Stats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class WorkStatsViewModel(
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val getBrowseEntityCountFlowUseCase: GetBrowseEntityCountFlowUseCase,
    private val recordingWorkDao: RecordingWorkDao,
) : ViewModel() {

    fun getStats(entityId: String): Flow<Stats> =
        combine(
            getCountOfEachRelationshipTypeUseCase(entityId),
            getBrowseEntityCountFlowUseCase(entityId, MusicBrainzEntity.RECORDING),
            recordingWorkDao.getNumberOfRecordingsByWork(entityId),
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
