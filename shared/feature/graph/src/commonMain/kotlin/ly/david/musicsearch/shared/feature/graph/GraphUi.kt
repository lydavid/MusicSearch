package ly.david.musicsearch.shared.feature.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.data2viz.viz.LineNode
import ly.david.musicsearch.shared.feature.graph.viz.render
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.getSubTextColor

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
        topBar = {
            ScrollableTopAppBar(
                showBackButton = true,
                onBack = {
                    eventSink(GraphUiEvent.NavigateUp)
                },
                title = strings.collaborationsWith(state.artistName),
            )
        },
    ) { innerPadding ->
        GraphUi(
            links = state.links,
            nodes = state.nodes,
            modifier = Modifier.padding(innerPadding),
            onClick = { node ->
                eventSink(
                    GraphUiEvent.ClickItem(
                        entity = node.entity,
                        id = node.id,
                        title = node.name,
                    ),
                )
            },
        )
    }
}

@Composable
internal fun GraphUi(
    links: List<LineNode>,
    nodes: List<GraphNode>,
    modifier: Modifier = Modifier,
    onClick: (node: GraphNode) -> Unit = { },
) {
    var panOffset by remember { mutableStateOf(Offset.Zero) }
    var center by remember { mutableStateOf(Offset.Zero) }
    val textMeasurer = rememberTextMeasurer()

    val isDark = isSystemInDarkTheme()
    val lineColor = getSubTextColor()

    Canvas(
        modifier = modifier
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

                    // TODO: support clicking on nodes
                    //  when in here, nodes is []
                    println(nodes)

                    val clickedNode = nodes.firstOrNull { node ->
                        val nodePosition = drawOffset + Offset(
                            node.x.toFloat(),
                            node.y.toFloat(),
                        )
                        (tapOffset - nodePosition).getDistance() <= node.radius
                    }

                    clickedNode?.let {
                        onClick(it)
                    }
                }
            },
    ) {
        center = this.center
        val drawOffset = panOffset + center

        links
            .forEach { node ->
                render(
                    lineNode = node,
                    offset = drawOffset,
                    color = lineColor,
                )
            }
        nodes
            .forEach { node ->
                render(
                    graphNode = node,
                    offset = drawOffset,
                )
                val measuredText =
                    textMeasurer.measure(
                        text = node.name,
                        constraints = Constraints.fixed(
                            width = (size.width / 3f).toInt(),
                            height = (size.height / 3f).toInt(),
                        ),
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(fontSize = 13.sp),
                    )

                drawText(
                    textLayoutResult = measuredText,
                    color = if (isDark) Color.White else Color.Black,
                    topLeft = Offset(
                        node.x.dp.toPx(),
                        node.y.dp.toPx(),
                    ) + drawOffset,
                )
            }
    }
}
