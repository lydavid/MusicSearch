package ly.david.musicsearch.share.feature.database

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap
import ly.david.musicsearch.shared.domain.area.AreasByEntityRepository
import ly.david.musicsearch.shared.domain.artist.ArtistsByEntityRepository
import ly.david.musicsearch.shared.domain.event.EventsByEntityRepository
import ly.david.musicsearch.shared.domain.genre.GenresByEntityRepository
import ly.david.musicsearch.shared.domain.instrument.InstrumentsByEntityRepository
import ly.david.musicsearch.shared.domain.label.LabelsByEntityRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.place.PlacesByEntityRepository
import ly.david.musicsearch.shared.domain.recording.RecordingsByEntityRepository
import ly.david.musicsearch.shared.domain.release.ReleasesByEntityRepository
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsByEntityRepository
import ly.david.musicsearch.shared.domain.series.SeriesByEntityRepository
import ly.david.musicsearch.shared.domain.work.WorksByEntityRepository
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class DatabasePresenter(
    private val navigator: Navigator,
    private val areasByEntityRepository: AreasByEntityRepository,
    private val artistsByEntityRepository: ArtistsByEntityRepository,
    private val eventsByEntityRepository: EventsByEntityRepository,
    private val genresByEntityRepository: GenresByEntityRepository,
    private val instrumentsByEntityRepository: InstrumentsByEntityRepository,
    private val labelsByEntityRepository: LabelsByEntityRepository,
    private val placesByEntityRepository: PlacesByEntityRepository,
    private val recordingsByEntityRepository: RecordingsByEntityRepository,
    private val releasesByEntityRepository: ReleasesByEntityRepository,
    private val releaseGroupsByEntityRepository: ReleaseGroupsByEntityRepository,
    private val seriesByEntityRepository: SeriesByEntityRepository,
    private val worksByEntityRepository: WorksByEntityRepository,
) : Presenter<DatabaseUiState> {
    @Composable
    override fun present(): DatabaseUiState {
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val lazyListState = rememberLazyListState()
        val countOfAllAreas by areasByEntityRepository.observeCountOfAllAreas().collectAsRetainedState(0)
        val countOfAllArtists by artistsByEntityRepository.observeCountOfAllArtists().collectAsRetainedState(0)
        val countOfAllEvents by eventsByEntityRepository.observeCountOfAllEvents().collectAsRetainedState(0)
        val countOfAllGenres by genresByEntityRepository.observeCountOfAllGenres().collectAsRetainedState(0)
        val countOfAllInstruments by instrumentsByEntityRepository.observeCountOfAllInstruments()
            .collectAsRetainedState(0)
        val countOfAllLabels by labelsByEntityRepository.observeCountOfAllLabels().collectAsRetainedState(0)
        val countOfAllPlaces by placesByEntityRepository.observeCountOfAllPlaces().collectAsRetainedState(0)
        val countOfAllRecordings by recordingsByEntityRepository.observeCountOfAllRecordings()
            .collectAsRetainedState(0)
        val countOfAllReleases by releasesByEntityRepository.observeCountOfAllReleases().collectAsRetainedState(0)
        val countOfAllReleaseGroups by releaseGroupsByEntityRepository.observeCountOfAllReleaseGroups()
            .collectAsRetainedState(0)
        val countOfAllSeries by seriesByEntityRepository.observeCountOfAllSeries().collectAsRetainedState(0)
        val countOfAllWorks by worksByEntityRepository.observeCountOfAllWorks().collectAsRetainedState(0)

        fun eventSink(event: DatabaseUiEvent) {
            when (event) {
                is DatabaseUiEvent.GoToScreen -> {
                    navigator.goTo(screen = event.screen)
                }
            }
        }

        return DatabaseUiState(
            topAppBarFilterState = topAppBarFilterState,
            lazyListState = lazyListState,
            entitiesCount = MusicBrainzEntity.entries.associateWith { entity ->
                when (entity) {
                    MusicBrainzEntity.AREA -> countOfAllAreas
                    MusicBrainzEntity.ARTIST -> countOfAllArtists
                    MusicBrainzEntity.EVENT -> countOfAllEvents
                    MusicBrainzEntity.GENRE -> countOfAllGenres
                    MusicBrainzEntity.INSTRUMENT -> countOfAllInstruments
                    MusicBrainzEntity.LABEL -> countOfAllLabels
                    MusicBrainzEntity.PLACE -> countOfAllPlaces
                    MusicBrainzEntity.RECORDING -> countOfAllRecordings
                    MusicBrainzEntity.RELEASE -> countOfAllReleases
                    MusicBrainzEntity.RELEASE_GROUP -> countOfAllReleaseGroups
                    MusicBrainzEntity.SERIES -> countOfAllSeries
                    MusicBrainzEntity.WORK -> countOfAllWorks
                    else -> 0
                }
            }.toImmutableMap(),
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class DatabaseUiState(
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val lazyListState: LazyListState = LazyListState(),
    val entitiesCount: ImmutableMap<MusicBrainzEntity, Long> = persistentMapOf(),
    val eventSink: (DatabaseUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface DatabaseUiEvent : CircuitUiEvent {
    data class GoToScreen(val screen: Screen) : DatabaseUiEvent
}
