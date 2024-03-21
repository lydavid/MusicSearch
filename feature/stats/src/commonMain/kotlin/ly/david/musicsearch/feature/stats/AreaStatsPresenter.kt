package ly.david.musicsearch.feature.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.toImmutableList
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.AreaEventDao
import ly.david.musicsearch.data.database.dao.AreaPlaceDao
import ly.david.musicsearch.data.database.dao.ReleaseCountryDao
import ly.david.musicsearch.domain.browse.usecase.ObserveBrowseEntityCount
import ly.david.musicsearch.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.feature.stats.internal.StatsUiState
import ly.david.ui.common.screen.StatsScreen

internal class AreaStatsPresenter(
    private val screen: StatsScreen,
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val observeBrowseEntityCount: ObserveBrowseEntityCount,
    private val releaseCountryDao: ReleaseCountryDao,
    private val areaEventDao: AreaEventDao,
    private val areaPlaceDao: AreaPlaceDao,
) : Presenter<StatsUiState> {

    @Composable
    override fun present(): StatsUiState {
        val relationTypeCounts
            by getCountOfEachRelationshipTypeUseCase(screen.id).collectAsState(listOf())

        val browseEventCount
            by observeBrowseEntityCount(
                entityId = screen.id,
                entity = MusicBrainzEntity.EVENT,
            ).collectAsState(null)
        val localEvents
            by areaEventDao.getNumberOfEventsByArea(screen.id).collectAsState(0)

        val browseReleaseCount
            by observeBrowseEntityCount(
                entityId = screen.id,
                entity = MusicBrainzEntity.RELEASE,
            ).collectAsState(null)
        val localReleases
            by releaseCountryDao.getNumberOfReleasesByCountry(screen.id).collectAsState(0)

        val browsePlaceCount
            by observeBrowseEntityCount(
                entityId = screen.id,
                entity = MusicBrainzEntity.PLACE,
            ).collectAsState(null)
        val localPlaces
            by areaPlaceDao.getNumberOfPlacesByArea(screen.id).collectAsState(0)

        val stats = Stats(
            totalRelations = relationTypeCounts.sumOf { it.count },
            relationTypeCounts = relationTypeCounts.toImmutableList(),
            eventStats = EventStats(
                totalRemote = browseEventCount?.remoteCount,
                totalLocal = localEvents,
            ),
            releaseStats = ReleaseStats(
                totalRemote = browseReleaseCount?.remoteCount,
                totalLocal = localReleases,
            ),
            placeStats = PlaceStats(
                totalRemote = browsePlaceCount?.remoteCount,
                totalLocal = localPlaces,
            ),
        )

        return StatsUiState(
            stats = stats,
            tabs = screen.tabs.toImmutableList(),
        )
    }
}
