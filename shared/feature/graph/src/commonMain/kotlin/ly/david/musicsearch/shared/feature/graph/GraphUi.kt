package ly.david.musicsearch.shared.feature.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import ly.david.musicsearch.shared.feature.graph.viz.render
import ly.david.musicsearch.ui.core.LocalStrings

@Composable
internal fun GraphUi(
    state: GraphUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val strings = LocalStrings.current

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->
        var panOffset by remember { mutableStateOf(Offset.Zero) }
        var center by remember { mutableStateOf(Offset.Zero) }

        Canvas(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        panOffset += dragAmount
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures { tapOffset ->
                        val drawOffset = panOffset + center
                        println(state.nodes)
                        val clickedNode = state.nodes.firstOrNull { node ->
                            val nodePosition = drawOffset + Offset(
                                node.x.toFloat(),
                                node.y.toFloat(),
                            )
                            (tapOffset - nodePosition).getDistance() <= node.radius
                        }

                        // TODO: need to hold data, not just circle
                        clickedNode?.let {
                            println("clicked")
//                                eventSink(GraphUiEvent.ClickItem(cl))
                        }
                    }
                },
        ) {
            center = this.center
            val drawOffset = panOffset + center

            state.links
                .forEach { node ->
                    render(
                        lineNode = node,
                        offset = drawOffset,
                    )
                }
            state.nodes
                .forEach { node ->
                    render(
                        graphNode = node,
                        offset = drawOffset,
                    )
                }
        }
    }
}
