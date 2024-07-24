package ly.david.musicsearch.shared.feature.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
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
            Box(
                Modifier
                    .fillMaxSize()
                    .drawBehind {
                        state.links
                            .forEach {
                                render(it)
                            }
                        state.nodes
//                            .map { node ->
//                                CircleNode(
//                                    circle = CircleGeom(
//                                        x = node.circle.x,
//                                        y = node.circle.y,
//                                        radius = node.circle.radius,
//                                    )
//                                )
//                            }
                            .forEach { node ->
                                render(
                                    node = node,
                                    offset = center,
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
