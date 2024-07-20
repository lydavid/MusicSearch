package ly.david.musicsearch.shared.feature.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.screen.CollectionScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen

internal class GraphPresenter(
    private val navigator: Navigator,
    private val graphSimulation: GraphSimulation,
) : Presenter<GraphUiState> {

    @Composable
    override fun present(): GraphUiState {
        val graphState by graphSimulation.uiState.collectAsState()
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            scope.launch {
                while (true) {
                    delay(16)
                    graphSimulation.run()
                }
            }
        }

        fun eventSink(event: GraphUiEvent) {
            when (event) {
                is GraphUiEvent.ClickItem -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            if (event.entity == MusicBrainzEntity.COLLECTION) {
                                CollectionScreen(
                                    id = event.id,
                                )
                            } else {
                                DetailsScreen(
                                    entity = event.entity,
                                    id = event.id,
                                    title = event.title,
                                )
                            },
                        ),
                    )
                }
            }
        }

        return GraphUiState(
            links = graphState.links,
            winds = graphState.winds,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class GraphUiState(
    val links: List<LineNode> = listOf(),
    val winds: List<CircleNode> = listOf(),
    val eventSink: (GraphUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface GraphUiEvent : CircuitUiEvent {
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : GraphUiEvent
}
