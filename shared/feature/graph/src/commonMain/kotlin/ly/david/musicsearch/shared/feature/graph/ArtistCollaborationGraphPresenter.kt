package ly.david.musicsearch.shared.feature.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.models.artist.CollaboratingArtistAndRecording
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.artist.ArtistRepository
import ly.david.musicsearch.ui.common.screen.ArtistCollaborationScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen

private const val DELAY_FOR_60_FPS_IN_MS = 16L

internal class ArtistCollaborationGraphPresenter(
    private val screen: ArtistCollaborationScreen,
    private val navigator: Navigator,
    private val graphSimulation: ArtistCollaborationGraphSimulation,
    private val artistRepository: ArtistRepository,
) : Presenter<ArtistCollaborationGraphUiState> {

    @Composable
    override fun present(): ArtistCollaborationGraphUiState {
        val graphState by graphSimulation.uiState.collectAsState()
        val scope = rememberCoroutineScope()

        val collaboratingArtistsAndRecordings: List<CollaboratingArtistAndRecording> by remember {
            mutableStateOf(artistRepository.getAllCollaboratingArtistsAndRecordings(screen.id))
        }

        LaunchedEffect(screen.id) {
            graphSimulation.initialize(
                collaboratingArtistAndRecordings = collaboratingArtistsAndRecordings,
            )
            scope.launch {
                while (true) {
                    delay(DELAY_FOR_60_FPS_IN_MS)
                    graphSimulation.step()
                }
            }
        }

        fun eventSink(event: ArtistCollaborationGraphUiEvent) {
            when (event) {
                is ArtistCollaborationGraphUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is ArtistCollaborationGraphUiEvent.ClickItem -> {
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

        return ArtistCollaborationGraphUiState(
            artistName = screen.name,
            links = graphState.links,
            nodes = graphState.nodes,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class ArtistCollaborationGraphUiState(
    val artistName: String,
    val links: List<GraphLink> = listOf(),
    val nodes: List<GraphNode> = listOf(),
    val eventSink: (ArtistCollaborationGraphUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface ArtistCollaborationGraphUiEvent : CircuitUiEvent {
    data object NavigateUp : ArtistCollaborationGraphUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : ArtistCollaborationGraphUiEvent
}
