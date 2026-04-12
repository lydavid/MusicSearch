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
import ly.david.musicsearch.shared.domain.image.MusicBrainzImageMetadataRepository
import ly.david.musicsearch.shared.domain.list.ObserveLocalCountsOfAllEntities
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class DatabasePresenter(
    private val navigator: Navigator,
    private val musicBrainzImageMetadataRepository: MusicBrainzImageMetadataRepository,
    private val observeLocalCountsOfAllEntities: ObserveLocalCountsOfAllEntities,
    private val appPreferences: AppPreferences,
) : Presenter<DatabaseUiState> {
    @Composable
    override fun present(): DatabaseUiState {
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val lazyListState = rememberLazyListState()
        val countOfAllImages by
            musicBrainzImageMetadataRepository.observeCountOfAllImageMetadata().collectAsRetainedState(null)
        val entitiesToCounts by observeLocalCountsOfAllEntities().collectAsRetainedState(listOf())
        val scrollToHideTopAppBar by appPreferences.scrollToHideTopAppBar.collectAsRetainedState(false)

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
            entitiesCount = entitiesToCounts.toMap().toImmutableMap(),
            scrollToHideTopAppBar = scrollToHideTopAppBar,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class DatabaseUiState(
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val lazyListState: LazyListState = LazyListState(),
    val countOfAllImages: Long? = null,
    val entitiesCount: ImmutableMap<MusicBrainzEntityType, Long> = persistentMapOf(),
    val scrollToHideTopAppBar: Boolean = false,
    val eventSink: (DatabaseUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface DatabaseUiEvent : CircuitUiEvent {
    data class GoToScreen(val screen: Screen) : DatabaseUiEvent
}
