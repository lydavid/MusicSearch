package ly.david.musicsearch.shared.feature.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.delay
import ly.david.musicsearch.shared.domain.artist.ArtistCollaborationRepository
import ly.david.musicsearch.shared.domain.artist.CollaboratingArtistAndEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.ui.common.screen.ArtistCollaborationScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

private const val DELAY_FOR_60_FPS_IN_MS = 16L

internal class ArtistCollaborationGraphPresenter(
    private val screen: ArtistCollaborationScreen,
    private val navigator: Navigator,
    private val graphSimulation: ArtistCollaborationGraphSimulation,
    private val artistCollaborationRepository: ArtistCollaborationRepository,
    private val appPreferences: AppPreferences,
) : Presenter<ArtistCollaborationGraphUiState> {

    @Composable
    override fun present(): ArtistCollaborationGraphUiState {
        val graphState by graphSimulation.uiState.collectAsRetainedState()
        val collaborationEntityType by
            appPreferences.observeCollaborationEntityType.collectAsState(initial = MusicBrainzEntity.RECORDING)
        val isDeveloperMode by appPreferences.isDeveloperMode.collectAsState(initial = false)

        val topAppBarFilterState = rememberTopAppBarFilterState()
        val collaboratingArtistsAndEntities: List<CollaboratingArtistAndEntity> by rememberRetained(
            collaborationEntityType,
            topAppBarFilterState.filterText,
        ) {
            mutableStateOf(
                artistCollaborationRepository.getAllCollaboratingArtistsAndEntities(
                    artistId = screen.id,
                    collaborationEntityType = collaborationEntityType,
                    query = topAppBarFilterState.filterText,
                ),
            )
        }

        LaunchedEffect(
            key1 = collaboratingArtistsAndEntities,
        ) {
            graphSimulation.initialize(
                collaboratingArtistsAndEntities = collaboratingArtistsAndEntities,
            )
            while (graphSimulation.step()) {
                delay(DELAY_FOR_60_FPS_IN_MS)
            }
        }

        fun eventSink(event: ArtistCollaborationGraphUiEvent) {
            when (event) {
                is ArtistCollaborationGraphUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is ArtistCollaborationGraphUiEvent.SelectEntity -> {
                    appPreferences.setCollaborationEntityType(event.entity)
                }

                is ArtistCollaborationGraphUiEvent.ClickItem -> {
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

        return ArtistCollaborationGraphUiState(
            artistName = screen.name,
            topAppBarFilterState = topAppBarFilterState,
            collaborationEntityType = collaborationEntityType,
            edges = graphState.edges,
            nodes = graphState.nodes,
            isDeveloperMode = isDeveloperMode,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class ArtistCollaborationGraphUiState(
    val artistName: String,
    val topAppBarFilterState: TopAppBarFilterState,
    val collaborationEntityType: MusicBrainzEntity = MusicBrainzEntity.RECORDING,
    val edges: List<GraphEdge> = listOf(),
    val nodes: List<GraphNode> = listOf(),
    val isDeveloperMode: Boolean = false,
    val eventSink: (ArtistCollaborationGraphUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface ArtistCollaborationGraphUiEvent : CircuitUiEvent {
    data object NavigateUp : ArtistCollaborationGraphUiEvent
    data class SelectEntity(val entity: MusicBrainzEntity) : ArtistCollaborationGraphUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
    ) : ArtistCollaborationGraphUiEvent
}
