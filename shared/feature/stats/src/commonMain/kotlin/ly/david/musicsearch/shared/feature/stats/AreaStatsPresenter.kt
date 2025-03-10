package ly.david.musicsearch.shared.feature.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.shared.domain.browse.usecase.ObserveBrowseEntityCount
import ly.david.musicsearch.shared.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.shared.feature.stats.internal.StatsUiState
import ly.david.musicsearch.ui.common.screen.StatsScreen

internal class AreaStatsPresenter(
    private val screen: StatsScreen,
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val observeBrowseEntityCount: ObserveBrowseEntityCount,
    private val releaseDao: ReleaseDao,
    private val artistDao: ArtistDao,
    private val eventDao: EventDao,
    private val labelDao: LabelDao,
    private val placeDao: PlaceDao,
) : Presenter<StatsUiState> {

    @Composable
    override fun present(): StatsUiState {
        val relationTypeCounts
            by getCountOfEachRelationshipTypeUseCase(screen.id).collectAsState(listOf())

        val artistStats by artistStats(screen.id).collectAsState(ArtistStats())
        val eventStats by eventStats(screen.id).collectAsState(EventStats())
        val labelStats by labelStats(screen.id).collectAsState(LabelStats())
        val releaseStats by releaseStats(screen.id).collectAsState(ReleaseStats())
        val placeStats by placeStats(screen.id).collectAsState(PlaceStats())

        val stats = Stats(
            totalRelations = relationTypeCounts.sumOf { it.count },
            relationTypeCounts = relationTypeCounts.toImmutableList(),
            artistStats = artistStats,
            eventStats = eventStats,
            labelStats = labelStats,
            placeStats = placeStats,
            releaseStats = releaseStats,
        )

        return StatsUiState(
            stats = stats,
            tabs = screen.tabs.toImmutableList(),
        )
    }

    private fun artistStats(entityId: String): Flow<ArtistStats> =
        combine(
            observeBrowseEntityCount(
                entityId = entityId,
                entity = MusicBrainzEntity.ARTIST,
            ),
            artistDao.observeCountOfArtistsByEntity(entityId),
        ) { browseEntityCount, localCount ->
            ArtistStats(
                totalRemote = browseEntityCount?.remoteCount,
                totalLocal = localCount,
            )
        }

    private fun eventStats(entityId: String): Flow<EventStats> =
        combine(
            observeBrowseEntityCount(
                entityId = entityId,
                entity = MusicBrainzEntity.EVENT,
            ),
            eventDao.observeCountOfEventsByEntity(entityId),
        ) { browseEntityCount, localCount ->
            EventStats(
                totalRemote = browseEntityCount?.remoteCount,
                totalLocal = localCount,
            )
        }

    private fun labelStats(entityId: String): Flow<LabelStats> =
        combine(
            observeBrowseEntityCount(
                entityId = entityId,
                entity = MusicBrainzEntity.LABEL,
            ),
            labelDao.observeCountOfLabelsByEntity(entityId),
        ) { browseEntityCount, localCount ->
            LabelStats(
                totalRemote = browseEntityCount?.remoteCount,
                totalLocal = localCount,
            )
        }

    private fun placeStats(entityId: String): Flow<PlaceStats> =
        combine(
            observeBrowseEntityCount(
                entityId = entityId,
                entity = MusicBrainzEntity.PLACE,
            ),
            placeDao.observeCountOfPlacesByArea(entityId),
        ) { browseEntityCount, localCount ->
            PlaceStats(
                totalRemote = browseEntityCount?.remoteCount,
                totalLocal = localCount,
            )
        }

    private fun releaseStats(entityId: String): Flow<ReleaseStats> =
        combine(
            observeBrowseEntityCount(
                entityId,
                MusicBrainzEntity.RELEASE,
            ),
            releaseDao.observeCountOfReleasesByCountry(entityId),
        ) { browseEntityCount, localCount ->
            ReleaseStats(
                totalRemote = browseEntityCount?.remoteCount,
                totalLocal = localCount,
            )
        }
}
