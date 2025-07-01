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
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState
import ly.david.musicsearch.ui.common.screen.AllEntitiesScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.EntitiesListPresenter
import ly.david.musicsearch.ui.common.screen.EntitiesListUiEvent
import ly.david.musicsearch.ui.common.screen.EntitiesListUiState
import ly.david.musicsearch.ui.common.topappbar.TopAppBarEditState
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarEditState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.toTab

internal class AllEntitiesPresenter(
    private val screen: AllEntitiesScreen,
    private val navigator: Navigator,
    private val entitiesListPresenter: EntitiesListPresenter,
    private val loginPresenter: LoginPresenter,
) : Presenter<AllEntitiesUiState> {

    @Composable
    override fun present(): AllEntitiesUiState {
        var subtitle: String by rememberSaveable { mutableStateOf("") }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        val topAppBarEditState: TopAppBarEditState = rememberTopAppBarEditState()

        val entitiesListUiState = entitiesListPresenter.present()
        val entitiesListEventSink = entitiesListUiState.eventSink
        val loginUiState = loginPresenter.present()

        LaunchedEffect(
            key1 = query,
        ) {
            val browseMethod = BrowseMethod.All
            entitiesListEventSink(
                EntitiesListUiEvent.Get(
                    tab = screen.entity.toTab(),
                    browseMethod = browseMethod,
                    query = query,
                    isRemote = false,
                ),
            )
        }

        fun eventSink(event: AllEntitiesUiEvent) {
            when (event) {
                is AllEntitiesUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is AllEntitiesUiEvent.ClickItem -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            DetailsScreen(
                                entity = event.entity,
                                id = event.id,
                                title = event.title,
                            ),
                        ),
                    )
                }
            }
        }

        return AllEntitiesUiState(
            subtitle = subtitle,
            entity = screen.entity,
            topAppBarFilterState = topAppBarFilterState,
            topAppBarEditState = topAppBarEditState,
            entitiesListUiState = entitiesListUiState,
            loginUiState = loginUiState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class AllEntitiesUiState(
    val subtitle: String,
    val entity: MusicBrainzEntity,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val topAppBarEditState: TopAppBarEditState,
    val entitiesListUiState: EntitiesListUiState,
    val loginUiState: LoginUiState = LoginUiState(),
    val eventSink: (AllEntitiesUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface AllEntitiesUiEvent : CircuitUiEvent {
    data object NavigateUp : AllEntitiesUiEvent

    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : AllEntitiesUiEvent
}
