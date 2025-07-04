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
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.area.AreasListRepository
import ly.david.musicsearch.shared.domain.artist.ArtistsListRepository
import ly.david.musicsearch.shared.domain.event.EventsListRepository
import ly.david.musicsearch.shared.domain.genre.GenresListRepository
import ly.david.musicsearch.shared.domain.image.MusicBrainzImageMetadataRepository
import ly.david.musicsearch.shared.domain.instrument.InstrumentsListRepository
import ly.david.musicsearch.shared.domain.label.LabelsListRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.place.PlacesListRepository
import ly.david.musicsearch.shared.domain.recording.RecordingsListRepository
import ly.david.musicsearch.shared.domain.release.ReleasesListRepository
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsListRepository
import ly.david.musicsearch.shared.domain.series.SeriesListRepository
import ly.david.musicsearch.shared.domain.work.WorksListRepository
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class DatabasePresenter(
    private val navigator: Navigator,
    private val musicBrainzImageMetadataRepository: MusicBrainzImageMetadataRepository,
    private val areasListRepository: AreasListRepository,
    private val artistsListRepository: ArtistsListRepository,
    private val eventsListRepository: EventsListRepository,
    private val genresListRepository: GenresListRepository,
    private val instrumentsListRepository: InstrumentsListRepository,
    private val labelsListRepository: LabelsListRepository,
    private val placesListRepository: PlacesListRepository,
    private val recordingsListRepository: RecordingsListRepository,
    private val releasesListRepository: ReleasesListRepository,
    private val releaseGroupsListRepository: ReleaseGroupsListRepository,
    private val seriesListRepository: SeriesListRepository,
    private val worksListRepository: WorksListRepository,
) : Presenter<DatabaseUiState> {
    @Composable
    override fun present(): DatabaseUiState {
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val lazyListState = rememberLazyListState()
        val countOfAllImages by
            musicBrainzImageMetadataRepository.observeCountOfAllImageMetadata().collectAsRetainedState(0)
        val countOfAllAreas by areasListRepository.observeCountOfAreas(
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllArtists by artistsListRepository.observeCountOfArtists(
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllEvents by eventsListRepository.observeCountOfEvents(
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllGenres by genresListRepository.observeCountOfGenres(
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllInstruments by instrumentsListRepository.observeCountOfInstruments(
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllLabels by labelsListRepository.observeCountOfLabels(
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllPlaces by placesListRepository.observeCountOfPlaces(
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllRecordings by recordingsListRepository.observeCountOfRecordings(
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllReleases by releasesListRepository.observeCountOfReleases(
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllReleaseGroups by releaseGroupsListRepository.observeCountOfReleaseGroups(
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllSeries by seriesListRepository.observeCountOfSeries(
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllWorks by worksListRepository.observeCountOfWorks(
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)

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
            countOfAllImages = countOfAllImages,
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
    val countOfAllImages: Long = 0,
    val entitiesCount: ImmutableMap<MusicBrainzEntity, Int> = persistentMapOf(),
    val eventSink: (DatabaseUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface DatabaseUiEvent : CircuitUiEvent {
    data class GoToScreen(val screen: Screen) : DatabaseUiEvent
}
