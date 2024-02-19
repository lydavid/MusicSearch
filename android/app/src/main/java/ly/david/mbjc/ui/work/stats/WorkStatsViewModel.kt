package ly.david.mbjc.ui.work.stats

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.RecordingWorkDao
import ly.david.musicsearch.domain.browse.usecase.ObserveBrowseEntityCount
import ly.david.musicsearch.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.feature.stats.RecordingStats
import ly.david.musicsearch.feature.stats.Stats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class WorkStatsViewModel(
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val observeBrowseEntityCount: ObserveBrowseEntityCount,
    private val recordingWorkDao: RecordingWorkDao,
) : ViewModel() {

    fun getStats(entityId: String): Flow<ly.david.musicsearch.feature.stats.Stats> =
        combine(
            getCountOfEachRelationshipTypeUseCase(entityId),
            observeBrowseEntityCount(entityId, MusicBrainzEntity.RECORDING),
            recordingWorkDao.getNumberOfRecordingsByWork(entityId),
        ) { relationTypeCounts, browseRecordingCount, localRecordings ->
            ly.david.musicsearch.feature.stats.Stats(
                totalRelations = relationTypeCounts.sumOf { it.count },
                relationTypeCounts = relationTypeCounts.toImmutableList(),
                recordingStats = ly.david.musicsearch.feature.stats.RecordingStats(
                    totalRemote = browseRecordingCount?.remoteCount,
                    totalLocal = localRecordings,
                ),
            )
        }
            .distinctUntilChanged()
}
