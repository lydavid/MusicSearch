package ly.david.musicsearch.shared.feature.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ly.david.musicsearch.shared.feature.graph.viz.compose.renderEdge
import ly.david.musicsearch.shared.feature.graph.viz.compose.renderNode
import ly.david.musicsearch.shared.feature.graph.viz.compose.renderText
import ly.david.musicsearch.ui.common.topappbar.OverflowMenuScope
import ly.david.musicsearch.ui.common.topappbar.ToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.ExtendedColors
import ly.david.musicsearch.ui.core.theme.LocalExtendedColors
import ly.david.musicsearch.ui.core.theme.getSubTextColor

private const val MIN_SCALE = 0.1f
private const val MAX_SCALE = 3f

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

    var showDebugInfo by remember { mutableStateOf(false) }

    val overflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)? = if (state.isDeveloperMode) {
        {
            ToggleMenuItem(
                toggleOnText = "Show debug info",
                toggleOffText = "Hide debug info",
                onToggle = {
                    showDebugInfo = it
                },
                toggled = showDebugInfo,
            )
        }
    } else {
        null
    }

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
                overflowDropdownMenuItems = overflowDropdownMenuItems,
            )
        },
    ) { innerPadding ->
        ArtistCollaborationGraphUi(
            edges = state.edges,
            nodes = state.nodes,
            filterText = state.topAppBarFilterState.filterText,
            modifier = Modifier.padding(innerPadding),
            showDebugInfo = showDebugInfo,
            onClick = { transformedTapPosition, _, _ ->
                with(density) {
                    // The tap position is already properly transformed to match node coordinate space
                    val clickedNode = state.nodes.firstOrNull { node ->
                        val nodePosition = Offset(
                            node.x.dp.toPx(),
                            node.y.dp.toPx(),
                        )
                        val nodeRadius = node.radius.dp.toPx()
                        (transformedTapPosition - nodePosition).getDistanceSquared() <= nodeRadius * nodeRadius
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
    showDebugInfo: Boolean = false,
    onClick: (tapOffset: Offset, drawOffset: Offset, zoomScale: Float) -> Unit = { _, _, _ -> },
) {
    var panOffset by remember { mutableStateOf(Offset.Zero) }
    var center by remember { mutableStateOf(Offset.Zero) }
    var zoomScale by remember { mutableFloatStateOf(1f) }
    val textMeasurer = rememberTextMeasurer()

    var lastTapPosition by remember { mutableStateOf<Offset?>(null) }
    var transformedTapPosition by remember { mutableStateOf<Offset?>(null) }

    val extendedColors: ExtendedColors = LocalExtendedColors.current
    val textColor = MaterialTheme.colorScheme.onBackground
    val edgeColor = getSubTextColor()

    val currentOnClick by rememberUpdatedState(onClick)

    LaunchedEffect(filterText) {
        panOffset = Offset.Zero
        center = Offset.Zero
        zoomScale = 1f
        lastTapPosition = null
        transformedTapPosition = null
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    // For pinch zoom - zoom around the pinch centroid
                    val oldScale = zoomScale
                    zoomScale = (zoomScale * zoom).coerceIn(MIN_SCALE, MAX_SCALE)

                    // Adjust pan to maintain focus point
                    val newScale = zoomScale
                    val focusPoint = centroid - center

                    // Calculate the focus point after scaling
                    val scaledFocusX = focusPoint.x * (newScale / oldScale)
                    val scaledFocusY = focusPoint.y * (newScale / oldScale)

                    // Adjust pan to keep the focus point under the finger
                    panOffset += Offset(
                        x = focusPoint.x - scaledFocusX,
                        y = focusPoint.y - scaledFocusY,
                    ) + pan
                }
            }
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    lastTapPosition = tapOffset

                    // Calculate inverse transformation manually in the correct order
                    // Step 1: Adjust for center (the origin for scaling)
                    var transformedTap = tapOffset - center

                    // Step 2: Inverse of scaling (divide by scale factor)
                    transformedTap = Offset(
                        x = transformedTap.x / zoomScale,
                        y = transformedTap.y / zoomScale,
                    )

                    // Step 3: Move back from origin
                    transformedTap += center

                    // Step 4: Remove panning effect (subtract pan offset)
                    transformedTap -= panOffset
                    transformedTapPosition = transformedTap

                    currentOnClick(
                        transformedTap,
                        Offset.Zero,
                        zoomScale,
                    )
                }
            },
    ) {
        center = this.center

        drawContext.canvas.save()
        drawContext.canvas.translate(center.x, center.y)
        drawContext.canvas.scale(zoomScale, zoomScale)
        drawContext.canvas.translate(-center.x, -center.y)
        drawContext.canvas.translate(panOffset.x, panOffset.y)

        edges.forEach { edge ->
            renderEdge(
                edge = edge,
                offset = Offset.Zero,
                color = edgeColor,
            )
        }

        // Draw debug info in transformed space
        if (showDebugInfo) {
            // Draw transformed tap position in graph space
            transformedTapPosition?.let { transformedTap ->
                drawCircle(
                    color = Color.Green,
                    center = transformedTap,
                    radius = 10.dp.toPx() / zoomScale,
                    alpha = 0.5f,
                )
            }
        }

        nodes.forEach { node ->
            renderNode(
                node = node,
                offset = Offset.Zero,
                extendedColors = extendedColors,
            )
            renderText(
                node = node,
                offset = Offset.Zero,
                color = textColor,
                textMeasurer = textMeasurer,
            )
        }

        // Restore canvas state after drawing transformed elements
        drawContext.canvas.restore()

        // Draw debug elements in screen space
        if (showDebugInfo) {
            // Draw original tap position in screen space
            lastTapPosition?.let { lastTap ->
                drawCircle(
                    color = Color.Red,
                    center = lastTap,
                    radius = 8.dp.toPx(),
                    alpha = 0.5f,
                )
            }

            val debugInfo = "Scale: $zoomScale"
            val debugInfoLayout = textMeasurer.measure(
                text = AnnotatedString(debugInfo),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 14.sp,
                    background = Color.White.copy(alpha = 0.7f),
                ),
            )
            drawText(
                textLayoutResult = debugInfoLayout,
                topLeft = Offset(10.dp.toPx(), 10.dp.toPx()),
            )

            // Draw coordinate info if we have a transformed tap
            transformedTapPosition?.let { transformedTap ->
                val coordInfo = "Tap: (${transformedTap.x}, ${transformedTap.y})"
                val coordInfoLayout = textMeasurer.measure(
                    text = AnnotatedString(coordInfo),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        background = Color.White.copy(alpha = 0.7f),
                    ),
                )
                drawText(
                    textLayoutResult = coordInfoLayout,
                    topLeft = Offset(10.dp.toPx(), 40.dp.toPx()),
                )
            }
        }
    }
}
