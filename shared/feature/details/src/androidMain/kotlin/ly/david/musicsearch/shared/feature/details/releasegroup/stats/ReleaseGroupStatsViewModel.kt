package ly.david.musicsearch.shared.feature.details.releasegroup.stats

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.domain.browse.usecase.ObserveBrowseEntityCount
import ly.david.musicsearch.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.feature.stats.ReleaseStats
import ly.david.musicsearch.feature.stats.Stats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleaseGroupStatsViewModel(
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val observeBrowseEntityCount: ObserveBrowseEntityCount,
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao,
) : ViewModel() {

    fun getStats(entityId: String): Flow<Stats> =
        combine(
            getCountOfEachRelationshipTypeUseCase(entityId),
            observeBrowseEntityCount(entityId, MusicBrainzEntity.RELEASE),
            releaseReleaseGroupDao.getNumberOfReleasesByReleaseGroup(entityId),
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
