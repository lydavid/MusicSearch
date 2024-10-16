package ly.david.musicsearch.shared.feature.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.feature.graph.viz.compose.renderEdge
import ly.david.musicsearch.shared.feature.graph.viz.compose.renderNode
import ly.david.musicsearch.shared.feature.graph.viz.compose.renderText
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.ExtendedColors
import ly.david.musicsearch.ui.core.theme.LocalExtendedColors
import ly.david.musicsearch.ui.core.theme.getSubTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ArtistCollaborationGraphUi(
    state: ArtistCollaborationGraphUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val focusManager = LocalFocusManager.current
    val strings = LocalStrings.current
    val density = LocalDensity.current

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                showBackButton = true,
                onBack = {
                    eventSink(ArtistCollaborationGraphUiEvent.NavigateUp)
                },
                title = strings.collaborationsWith(state.artistName),
                topAppBarFilterState = state.topAppBarFilterState,
            )
        },
    ) { innerPadding ->
        ArtistCollaborationGraphUi(
            edges = state.edges,
            nodes = state.nodes,
            filterText = state.topAppBarFilterState.filterText,
            modifier = Modifier.padding(innerPadding),
            onClick = { tapOffset, drawOffset ->
                with(density) {
                    val clickedNode = state.nodes.firstOrNull { node ->
                        val nodePosition = drawOffset + Offset(
                            node.x.dp.toPx(),
                            node.y.dp.toPx(),
                        )
                        (tapOffset - nodePosition).getDistanceSquared() <= node.radius.dp.toPx() * node.radius.dp.toPx()
                    }

                    clickedNode?.let { node ->
                        eventSink(
                            ArtistCollaborationGraphUiEvent.ClickItem(
                                entity = node.entity,
                                id = node.id,
                                title = node.name,
                            ),
                        )

                        // Not certain why clicking list items in rest of the screens auto-dismiss but not here
                        // This will unfortunately prevent refocus when popping off back stack
                        focusManager.clearFocus()
                    }
                }
            },
        )
    }
}

@Composable
internal fun ArtistCollaborationGraphUi(
    edges: List<GraphEdge>,
    nodes: List<GraphNode>,
    filterText: String,
    modifier: Modifier = Modifier,
    onClick: (tapOffset: Offset, drawOffset: Offset) -> Unit = { _, _ -> },
) {
    var panOffset by remember { mutableStateOf(Offset.Zero) }
    var center by remember { mutableStateOf(Offset.Zero) }
    val textMeasurer = rememberTextMeasurer()

    val extendedColors: ExtendedColors = LocalExtendedColors.current
    val textColor = MaterialTheme.colorScheme.onBackground
    val edgeColor = getSubTextColor()

    val currentOnClick by rememberUpdatedState(onClick)

    LaunchedEffect(filterText) {
        panOffset = Offset.Zero
        center = Offset.Zero
    }

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

                    currentOnClick(
                        tapOffset,
                        drawOffset,
                    )
                }
            },
    ) {
        center = this.center
        val drawOffset = panOffset + center

        edges.forEach { edge ->
            renderEdge(
                edge = edge,
                offset = drawOffset,
                color = edgeColor,
            )
        }
        nodes.forEach { node ->
            renderNode(
                node = node,
                offset = drawOffset,
                extendedColors = extendedColors,
            )
            renderText(
                node = node,
                offset = drawOffset,
                color = textColor,
                textMeasurer = textMeasurer,
            )
        }
    }
}
