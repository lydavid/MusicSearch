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
import io.data2viz.viz.CircleNode
import io.data2viz.viz.LineNode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.models.artist.CollaboratingArtistAndRecording
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.artist.ArtistRepository
import ly.david.musicsearch.ui.common.screen.ArtistCollaborationScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen

private const val DELAY_FOR_60_FPS_IN_MS = 16L

internal class GraphPresenter(
    private val screen: ArtistCollaborationScreen,
    private val navigator: Navigator,
    private val graphSimulation: GraphSimulation,
    private val artistRepository: ArtistRepository,
) : Presenter<GraphUiState> {

    @Composable
    override fun present(): GraphUiState {
        val graphState by graphSimulation.uiState.collectAsState()
        val scope = rememberCoroutineScope()

        val collaboratingArtistAndRecordings: List<CollaboratingArtistAndRecording> by remember {
            mutableStateOf(artistRepository.getAllCollaboratingArtists(screen.id))
        }

        LaunchedEffect(screen.id) {
            graphSimulation.initialize(
                collaborations = collaboratingArtistAndRecordings,
            )
            println(collaboratingArtistAndRecordings)
            scope.launch {
                while (true) {
                    delay(DELAY_FOR_60_FPS_IN_MS)
                    graphSimulation.step()
                }
            }
        }

        fun eventSink(event: GraphUiEvent) {
            when (event) {
                is GraphUiEvent.ClickItem -> {
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

        return GraphUiState(
            links = graphState.links,
            nodes = graphState.nodes,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class GraphUiState(
    val links: List<LineNode> = listOf(),
    val nodes: List<GraphNode> = listOf(),
    val eventSink: (GraphUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface GraphUiEvent : CircuitUiEvent {
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : GraphUiEvent
}
