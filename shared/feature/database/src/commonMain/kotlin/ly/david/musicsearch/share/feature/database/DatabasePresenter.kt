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
import ly.david.musicsearch.shared.domain.image.MusicBrainzImageMetadataRepository
import ly.david.musicsearch.shared.domain.list.ObserveLocalCount
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class DatabasePresenter(
    private val navigator: Navigator,
    private val musicBrainzImageMetadataRepository: MusicBrainzImageMetadataRepository,
    private val observeLocalCount: ObserveLocalCount,
) : Presenter<DatabaseUiState> {
    @Composable
    override fun present(): DatabaseUiState {
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val lazyListState = rememberLazyListState()
        val countOfAllImages by
            musicBrainzImageMetadataRepository.observeCountOfAllImageMetadata().collectAsRetainedState(0)
        val countOfAllAreas by observeLocalCount(
            browseEntity = MusicBrainzEntityType.AREA,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllArtists by observeLocalCount(
            browseEntity = MusicBrainzEntityType.ARTIST,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllEvents by observeLocalCount(
            browseEntity = MusicBrainzEntityType.EVENT,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllGenres by observeLocalCount(
            browseEntity = MusicBrainzEntityType.GENRE,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllInstruments by observeLocalCount(
            browseEntity = MusicBrainzEntityType.INSTRUMENT,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllLabels by observeLocalCount(
            browseEntity = MusicBrainzEntityType.LABEL,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllPlaces by observeLocalCount(
            browseEntity = MusicBrainzEntityType.PLACE,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllRecordings by observeLocalCount(
            browseEntity = MusicBrainzEntityType.RECORDING,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllReleases by observeLocalCount(
            browseEntity = MusicBrainzEntityType.RELEASE,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllReleaseGroups by observeLocalCount(
            browseEntity = MusicBrainzEntityType.RELEASE_GROUP,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllSeries by observeLocalCount(
            browseEntity = MusicBrainzEntityType.SERIES,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllWorks by observeLocalCount(
            browseEntity = MusicBrainzEntityType.WORK,
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
            entitiesCount = MusicBrainzEntityType.entries.associateWith { entity ->
                when (entity) {
                    MusicBrainzEntityType.AREA -> countOfAllAreas
                    MusicBrainzEntityType.ARTIST -> countOfAllArtists
                    MusicBrainzEntityType.EVENT -> countOfAllEvents
                    MusicBrainzEntityType.GENRE -> countOfAllGenres
                    MusicBrainzEntityType.INSTRUMENT -> countOfAllInstruments
                    MusicBrainzEntityType.LABEL -> countOfAllLabels
                    MusicBrainzEntityType.PLACE -> countOfAllPlaces
                    MusicBrainzEntityType.RECORDING -> countOfAllRecordings
                    MusicBrainzEntityType.RELEASE -> countOfAllReleases
                    MusicBrainzEntityType.RELEASE_GROUP -> countOfAllReleaseGroups
                    MusicBrainzEntityType.SERIES -> countOfAllSeries
                    MusicBrainzEntityType.WORK -> countOfAllWorks
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
    val entitiesCount: ImmutableMap<MusicBrainzEntityType, Int> = persistentMapOf(),
    val eventSink: (DatabaseUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface DatabaseUiEvent : CircuitUiEvent {
    data class GoToScreen(val screen: Screen) : DatabaseUiEvent
}
