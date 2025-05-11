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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
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
            onClick = { transformedTapPosition ->
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
    onClick: (transformedTapPosition: Offset) -> Unit = { _ -> },
) {
    var panOffset by remember { mutableStateOf(Offset.Zero) }
    var centerOfScreen by remember { mutableStateOf(Offset.Zero) }
    var centerOffset by remember { mutableStateOf(Offset.Zero) }
    var zoomScale by remember { mutableFloatStateOf(1f) }
    val textMeasurer = rememberTextMeasurer()

    var debugLastTapPosition by remember { mutableStateOf<Offset?>(null) }
    var debugTransformedTapPosition by remember { mutableStateOf<Offset?>(null) }

    val extendedColors: ExtendedColors = LocalExtendedColors.current
    val textColor = MaterialTheme.colorScheme.onBackground
    val edgeColor = getSubTextColor()

    val currentOnClick by rememberUpdatedState(onClick)

    LaunchedEffect(filterText) {
        panOffset = centerOfScreen
        centerOffset = Offset.Zero
        zoomScale = 1f
        debugLastTapPosition = null
        debugTransformedTapPosition = null
    }

    // We don't know this until after the canvas has been lay out
    LaunchedEffect(centerOfScreen) {
        panOffset = centerOfScreen
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
                    val focusPoint = centroid - centerOffset

                    // Calculate the focus point after scaling
                    val scaledFocusX = focusPoint.x * (newScale / oldScale)
                    val scaledFocusY = focusPoint.y * (newScale / oldScale)

                    // Adjust pan to keep the focus point under the finger
                    panOffset += Offset(
                        x = focusPoint.x - scaledFocusX,
                        y = focusPoint.y - scaledFocusY,
                    ) + pan * (1f / zoomScale)
                }
            }
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    debugLastTapPosition = tapOffset

                    // Calculate inverse transformations
                    // Step 1: Adjust for center (the origin for scaling)
                    var transformedTap = tapOffset - centerOffset

                    // Step 2: Inverse of scaling (divide by scale factor)
                    transformedTap = Offset(
                        x = transformedTap.x / zoomScale,
                        y = transformedTap.y / zoomScale,
                    )

                    // Step 3: Move back from origin
                    transformedTap += centerOffset

                    // Step 4: Remove panning effect (subtract pan offset)
                    transformedTap -= panOffset
                    debugTransformedTapPosition = transformedTap

                    currentOnClick(transformedTap)
                }
            },
    ) {
        if (centerOfScreen == Offset.Zero) {
            centerOfScreen = this.center
        }
        centerOffset = this.center

        drawContext.canvas.save()
        drawContext.canvas.translate(centerOffset.x, centerOffset.y)
        drawContext.canvas.scale(zoomScale, zoomScale)
        drawContext.canvas.translate(-centerOffset.x, -centerOffset.y)
        drawContext.canvas.translate(panOffset.x, panOffset.y)

        edges.forEach { edge ->
            renderEdge(
                edge = edge,
                offset = Offset.Zero,
                color = edgeColor,
            )
        }

        debugTransformSpaceInfo(
            showDebugInfo = showDebugInfo,
            debugTransformedTapPosition = debugTransformedTapPosition,
            zoomScale = zoomScale,
        )

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

        debugScreenSpaceInfo(
            showDebugInfo = showDebugInfo,
            debugLastTapPosition = debugLastTapPosition,
            zoomScale = zoomScale,
            textMeasurer = textMeasurer,
            debugTransformedTapPosition = debugTransformedTapPosition,
        )
    }
}

private fun DrawScope.debugTransformSpaceInfo(
    showDebugInfo: Boolean,
    debugTransformedTapPosition: Offset?,
    zoomScale: Float,
) {
    if (showDebugInfo) {
        // Draw transformed tap position in graph space
        debugTransformedTapPosition?.let { transformedTap ->
            drawCircle(
                color = Color.Green,
                center = transformedTap,
                radius = 10.dp.toPx() / zoomScale,
                alpha = 0.5f,
            )
        }

        // Draw the origin (0,0) which should be translated to start at the center of the screen
        drawCircle(
            color = Color.Blue,
            center = Offset.Zero,
            radius = 12.dp.toPx() / zoomScale,
            alpha = 0.8f,
        )
    }
}

private fun DrawScope.debugScreenSpaceInfo(
    showDebugInfo: Boolean,
    debugLastTapPosition: Offset?,
    zoomScale: Float,
    textMeasurer: TextMeasurer,
    debugTransformedTapPosition: Offset?,
) {
    if (showDebugInfo) {
        // Draw original tap position in screen space
        debugLastTapPosition?.let { lastTap ->
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

        debugTransformedTapPosition?.let { transformedTap ->
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
