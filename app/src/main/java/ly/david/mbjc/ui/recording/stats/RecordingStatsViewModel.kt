package ly.david.mbjc.ui.recording.stats

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.browse.GetBrowseEntityCountFlowUseCase
import ly.david.data.domain.relation.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.data.database.dao.RecordingReleaseDao
import ly.david.ui.stats.ReleaseStats
import ly.david.ui.stats.Stats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class RecordingStatsViewModel(
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val getBrowseEntityCountFlowUseCase: GetBrowseEntityCountFlowUseCase,
    private val recordingReleaseDao: RecordingReleaseDao,
) : ViewModel() {

    fun getStats(entityId: String): Flow<Stats> =
        combine(
            getCountOfEachRelationshipTypeUseCase(entityId),
            getBrowseEntityCountFlowUseCase(entityId, MusicBrainzEntity.RELEASE),
            recordingReleaseDao.getNumberOfReleasesByRecording(entityId),
        ) { relationTypeCounts, browseReleaseCount, localReleases ->
            Stats(
                totalRelations = relationTypeCounts.sumOf { it.count },
                relationTypeCounts = relationTypeCounts.toImmutableList(),
                releaseStats = ReleaseStats(
                    totalRemote = browseReleaseCount?.remoteCount,
                    totalLocal = localReleases,
                ),
            )
        }
            .distinctUntilChanged()
}
