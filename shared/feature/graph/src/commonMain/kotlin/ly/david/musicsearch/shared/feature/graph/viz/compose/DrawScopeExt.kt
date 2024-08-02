package ly.david.musicsearch.shared.feature.graph.viz.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.feature.graph.GraphEdge
import ly.david.musicsearch.shared.feature.graph.GraphNode
import ly.david.musicsearch.ui.core.theme.ExtendedColors

fun DrawScope.renderEdge(
    edge: GraphEdge,
    offset: Offset,
    color: Color,
) {
    val start = Offset(
        edge.x0.dp.toPx(),
        edge.y0.dp.toPx(),
    ) + offset
    val end = Offset(
        edge.x1.dp.toPx(),
        edge.y1.dp.toPx(),
    ) + offset
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = 1f,
    )
}

fun DrawScope.renderNode(
    node: GraphNode,
    offset: Offset,
    extendedColors: ExtendedColors,
) {
    with(node) {
        val r = radius.dp.toPx()
        val c = Offset(
            x.dp.toPx(),
            y.dp.toPx(),
        ) + offset
        drawCircle(
            color = entity.getNodeColor(extendedColors),
            radius = r,
            center = c,
        )
    }
}

fun DrawScope.renderText(
    node: GraphNode,
    offset: Offset,
    color: Color,
    textMeasurer: TextMeasurer,
) {
    val measuredText =
        textMeasurer.measure(
            text = node.name,
            constraints = Constraints.fixed(
                width = (size.width / 3f).toInt(),
                height = size.height.toInt(),
            ),
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(fontSize = 13.sp),
        )

    drawText(
        textLayoutResult = measuredText,
        color = color,
        topLeft = Offset(
            node.x.dp.toPx(),
            node.y.dp.toPx() + node.radius.dp.toPx(),
        ) + offset,
    )
}

private fun MusicBrainzEntity.getNodeColor(extendedColors: ExtendedColors): Color {
    return when (this) {
        MusicBrainzEntity.AREA -> extendedColors.area
        MusicBrainzEntity.ARTIST -> extendedColors.artist
        MusicBrainzEntity.COLLECTION -> extendedColors.collection
        MusicBrainzEntity.EVENT -> extendedColors.event
        MusicBrainzEntity.GENRE -> extendedColors.genre
        MusicBrainzEntity.INSTRUMENT -> extendedColors.instrument
        MusicBrainzEntity.LABEL -> extendedColors.label
        MusicBrainzEntity.PLACE -> extendedColors.place
        MusicBrainzEntity.RECORDING -> extendedColors.recording
        MusicBrainzEntity.RELEASE -> extendedColors.release
        MusicBrainzEntity.RELEASE_GROUP -> extendedColors.releaseGroup
        MusicBrainzEntity.SERIES -> extendedColors.series
        MusicBrainzEntity.WORK -> extendedColors.work
        MusicBrainzEntity.URL -> extendedColors.url
    }
}
