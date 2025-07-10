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
import ly.david.musicsearch.shared.domain.list.EntitiesListRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class DatabasePresenter(
    private val navigator: Navigator,
    private val musicBrainzImageMetadataRepository: MusicBrainzImageMetadataRepository,
    private val entitiesListRepository: EntitiesListRepository,
) : Presenter<DatabaseUiState> {
    @Composable
    override fun present(): DatabaseUiState {
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val lazyListState = rememberLazyListState()
        val countOfAllImages by
            musicBrainzImageMetadataRepository.observeCountOfAllImageMetadata().collectAsRetainedState(0)
        val countOfAllAreas by entitiesListRepository.observeLocalCount(
            browseEntity = MusicBrainzEntity.AREA,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllArtists by entitiesListRepository.observeLocalCount(
            browseEntity = MusicBrainzEntity.ARTIST,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllEvents by entitiesListRepository.observeLocalCount(
            browseEntity = MusicBrainzEntity.EVENT,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllGenres by entitiesListRepository.observeLocalCount(
            browseEntity = MusicBrainzEntity.GENRE,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllInstruments by entitiesListRepository.observeLocalCount(
            browseEntity = MusicBrainzEntity.INSTRUMENT,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllLabels by entitiesListRepository.observeLocalCount(
            browseEntity = MusicBrainzEntity.LABEL,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllPlaces by entitiesListRepository.observeLocalCount(
            browseEntity = MusicBrainzEntity.PLACE,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllRecordings by entitiesListRepository.observeLocalCount(
            browseEntity = MusicBrainzEntity.RECORDING,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllReleases by entitiesListRepository.observeLocalCount(
            browseEntity = MusicBrainzEntity.RELEASE,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllReleaseGroups by entitiesListRepository.observeLocalCount(
            browseEntity = MusicBrainzEntity.RELEASE_GROUP,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllSeries by entitiesListRepository.observeLocalCount(
            browseEntity = MusicBrainzEntity.SERIES,
            browseMethod = BrowseMethod.All,
        ).collectAsRetainedState(0)
        val countOfAllWorks by entitiesListRepository.observeLocalCount(
            browseEntity = MusicBrainzEntity.WORK,
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
