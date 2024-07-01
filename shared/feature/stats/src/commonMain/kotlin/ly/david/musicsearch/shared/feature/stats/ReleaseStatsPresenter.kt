package ly.david.musicsearch.shared.feature.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.ArtistsByEntityDao
import ly.david.musicsearch.shared.domain.browse.usecase.ObserveBrowseEntityCount
import ly.david.musicsearch.shared.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.shared.feature.stats.internal.StatsUiState
import ly.david.musicsearch.ui.common.screen.StatsScreen

internal class ReleaseStatsPresenter(
    private val screen: StatsScreen,
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val observeBrowseEntityCount: ObserveBrowseEntityCount,
    private val artistsByEntityDao: ArtistsByEntityDao,
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
            artistStats(entityId),
        ) { relationTypeCounts, artistStats ->
            Stats(
                totalRelations = relationTypeCounts.sumOf { it.count },
                relationTypeCounts = relationTypeCounts.toImmutableList(),
                artistStats = artistStats,
            )
        }
            .distinctUntilChanged()

    private fun artistStats(entityId: String): Flow<ArtistStats> =
        combine(
            observeBrowseEntityCount(
                entityId,
                MusicBrainzEntity.ARTIST,
            ),
            artistsByEntityDao.getNumberOfArtistsByEntity(entityId),
        ) { browseRecordingCount, localRecordings ->
            ArtistStats(
                totalRemote = browseRecordingCount?.remoteCount,
                totalLocal = localRecordings,
            )
        }
}
