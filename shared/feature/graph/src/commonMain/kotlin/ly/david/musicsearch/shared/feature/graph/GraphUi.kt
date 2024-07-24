package ly.david.musicsearch.shared.feature.graph

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import ly.david.musicsearch.shared.feature.graph.viz.render
import ly.david.musicsearch.ui.core.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            var panOffset by remember { mutableStateOf(Offset.Zero) }

            Box(
                Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            panOffset += dragAmount
                        }
                    }
                    .drawBehind {
                        val drawOffset = center + panOffset

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
                                    circleNode = node,
                                    offset = drawOffset,
                                )
                            }
                    },
            ) {
            }

            Text(
                text = "Wind Simulation",
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally),
            )
        }
    }
}
