package ly.david.musicsearch.shared.feature.graph.viz.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.feature.graph.GraphEdge
import ly.david.musicsearch.shared.feature.graph.GraphNode
import ly.david.musicsearch.ui.common.theme.ExtendedColors

fun DrawScope.renderEdge(
    edge: GraphEdge,
    color: Color,
) {
    val start = Offset(
        edge.x0.dp.toPx(),
        edge.y0.dp.toPx(),
    )
    val end = Offset(
        edge.x1.dp.toPx(),
        edge.y1.dp.toPx(),
    )
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = 1f,
    )
}

fun DrawScope.renderNode(
    node: GraphNode,
    extendedColors: ExtendedColors,
) {
    with(node) {
        val r = radius.dp.toPx()
        val c = Offset(
            x.dp.toPx(),
            y.dp.toPx(),
        )
        drawCircle(
            color = entityType.getNodeColor(extendedColors),
            radius = r,
            center = c,
        )
    }
}

fun DrawScope.renderText(
    node: GraphNode,
    color: Color,
    textMeasurer: TextMeasurer,
) {
    val measuredText =
        textMeasurer.measure(
            text = node.name,
            constraints = Constraints(
                maxWidth = (size.width / 3f).toInt(),
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            style = TextStyle(
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
            ),
        )
    val textWidth = measuredText.size.width

    drawText(
        textLayoutResult = measuredText,
        color = color,
        topLeft = Offset(
            node.x.dp.toPx() - textWidth / 2f,
            node.y.dp.toPx() + node.radius.dp.toPx() + 4.dp.toPx(),
        ),
    )
}

private fun MusicBrainzEntityType.getNodeColor(extendedColors: ExtendedColors): Color {
    return when (this) {
        MusicBrainzEntityType.AREA -> extendedColors.area
        MusicBrainzEntityType.ARTIST -> extendedColors.artist
        MusicBrainzEntityType.COLLECTION -> extendedColors.collection
        MusicBrainzEntityType.EVENT -> extendedColors.event
        MusicBrainzEntityType.GENRE -> extendedColors.genre
        MusicBrainzEntityType.INSTRUMENT -> extendedColors.instrument
        MusicBrainzEntityType.LABEL -> extendedColors.label
        MusicBrainzEntityType.PLACE -> extendedColors.place
        MusicBrainzEntityType.RECORDING -> extendedColors.recording
        MusicBrainzEntityType.RELEASE -> extendedColors.release
        MusicBrainzEntityType.RELEASE_GROUP -> extendedColors.releaseGroup
        MusicBrainzEntityType.SERIES -> extendedColors.series
        MusicBrainzEntityType.WORK -> extendedColors.work
        MusicBrainzEntityType.URL -> extendedColors.url
    }
}
