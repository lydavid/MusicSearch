package ly.david.mbjc.ui.label.stats

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.ReleaseLabelDao
import ly.david.musicsearch.domain.browse.usecase.GetBrowseEntityCountFlowUseCase
import ly.david.musicsearch.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.ui.stats.ReleaseStats
import ly.david.ui.stats.Stats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LabelStatsViewModel(
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val getBrowseEntityCountFlowUseCase: GetBrowseEntityCountFlowUseCase,
    private val releaseLabelDao: ReleaseLabelDao,
) : ViewModel() {

    fun getStats(entityId: String): Flow<Stats> =
        combine(
            getCountOfEachRelationshipTypeUseCase(entityId),
            getBrowseEntityCountFlowUseCase(entityId, MusicBrainzEntity.RELEASE),
            releaseLabelDao.getNumberOfReleasesByLabel(entityId),
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
