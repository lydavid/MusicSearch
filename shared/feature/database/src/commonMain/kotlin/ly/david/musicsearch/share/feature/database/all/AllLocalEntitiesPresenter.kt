package ly.david.musicsearch.share.feature.database.all

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.list.AllEntitiesListPresenter
import ly.david.musicsearch.ui.common.list.AllEntitiesListUiEvent
import ly.david.musicsearch.ui.common.list.AllEntitiesListUiState
import ly.david.musicsearch.ui.common.list.getTotalLocalCount
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState
import ly.david.musicsearch.ui.common.screen.AllEntitiesScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.SelectionState
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberSelectionState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.toTab

internal class AllLocalEntitiesPresenter(
    private val screen: AllEntitiesScreen,
    private val navigator: Navigator,
    private val allEntitiesListPresenter: AllEntitiesListPresenter,
    private val loginPresenter: LoginPresenter,
) : Presenter<AllLocalEntitiesUiState> {

    @Composable
    override fun present(): AllLocalEntitiesUiState {
        var subtitle: String by rememberSaveable { mutableStateOf("") }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText

        val entitiesListUiState = allEntitiesListPresenter.present()
        val entitiesListEventSink = entitiesListUiState.eventSink

        val selectionState = rememberSelectionState(
            totalCount = entitiesListUiState.getTotalLocalCount(screen.entity),
        )

        val loginUiState = loginPresenter.present()

        LaunchedEffect(
            key1 = query,
        ) {
            val browseMethod = BrowseMethod.All
            entitiesListEventSink(
                AllEntitiesListUiEvent.Get(
                    tab = screen.entity.toTab(),
                    browseMethod = browseMethod,
                    query = query,
                    isRemote = false,
                ),
            )
        }

        fun eventSink(event: AllLocalEntitiesUiEvent) {
            when (event) {
                is AllLocalEntitiesUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is AllLocalEntitiesUiEvent.ClickItem -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            DetailsScreen(
                                entity = event.entity,
                                id = event.id,
                            ),
                        ),
                    )
                }
            }
        }

        return AllLocalEntitiesUiState(
            subtitle = subtitle,
            entity = screen.entity,
            topAppBarFilterState = topAppBarFilterState,
            selectionState = selectionState,
            allEntitiesListUiState = entitiesListUiState,
            loginUiState = loginUiState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class AllLocalEntitiesUiState(
    val subtitle: String,
    val entity: MusicBrainzEntity,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val selectionState: SelectionState = SelectionState(),
    val allEntitiesListUiState: AllEntitiesListUiState,
    val loginUiState: LoginUiState = LoginUiState(),
    val eventSink: (AllLocalEntitiesUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface AllLocalEntitiesUiEvent : CircuitUiEvent {
    data object NavigateUp : AllLocalEntitiesUiEvent

    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
    ) : AllLocalEntitiesUiEvent
}
