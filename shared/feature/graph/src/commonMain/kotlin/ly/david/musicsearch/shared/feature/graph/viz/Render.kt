/*
 * Copyright 2022-2022 Prat Tana
 * Copyright 2024 David Ly
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ly.david.musicsearch.shared.feature.graph.viz

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import io.data2viz.color.Color
import io.data2viz.color.ColorOrGradient
import io.data2viz.color.LinearGradient
import io.data2viz.color.RadialGradient
import io.data2viz.viz.LineNode
import io.data2viz.viz.RectNode
import ly.david.musicsearch.shared.feature.graph.GraphNode
import ly.david.musicsearch.shared.feature.graph.getNodeColor
import androidx.compose.ui.graphics.Color as ComposeColor

fun DrawScope.render(
    lineNode: LineNode,
    offset: Offset,
    color: androidx.compose.ui.graphics.Color,
) {
    lineNode.strokeColor?.let {
        val start = Offset(
            lineNode.x1.dp.toPx(),
            lineNode.y1.dp.toPx(),
        ) + offset
        val end = Offset(
            lineNode.x2.dp.toPx(),
            lineNode.y2.dp.toPx(),
        ) + offset
        drawLine(
            color = color,
            start = start,
            end = end,
            strokeWidth = lineNode.strokeWidth?.toFloat() ?: Stroke.HairlineWidth,
        )
    }
}

fun DrawScope.render(node: RectNode) {
    node.fill?.let {
        render(
            node,
            it,
            Fill,
        )
    }
    node.strokeColor?.let { colorOrGradient ->
        node.strokeWidth?.let {
            val stroke = node.strokeWidth?.toStroke(this@render) ?: Stroke()
            render(
                node,
                colorOrGradient,
                stroke,
            )
        }
    }
}

private fun DrawScope.render(
    node: RectNode,
    colorOrGradient: ColorOrGradient,
    style: DrawStyle,
) {
    val topLeft = Offset(
        node.x.dp.toPx(),
        node.y.dp.toPx(),
    )
    val s = Size(
        node.width.dp.toPx(),
        node.height.dp.toPx(),
    )
    when (colorOrGradient) {
        is Color -> drawRect(
            colorOrGradient.toComposeColor(),
            topLeft,
            s,
            style = style,
        )

        is LinearGradient -> drawRect(
            colorOrGradient.toBrush(this),
            topLeft,
            s,
            style = style,
        )

        is RadialGradient -> drawRect(
            colorOrGradient.toBrush(this),
            topLeft,
            s,
            style = style,
        )
    }
}

fun DrawScope.render(
    graphNode: GraphNode,
    offset: Offset,
) {
    with(graphNode) {
        val r = radius.dp.toPx()
        val c = Offset(
            x.dp.toPx(),
            y.dp.toPx(),
        ) + offset
        drawCircle(
            color = entity.getNodeColor().toComposeColor(),
            radius = r,
            center = c,
        )
    }
}

private fun Color.toComposeColor(): ComposeColor =
    ComposeColor(
        (255 * alpha.value).toInt() and 0xff shl 24 or
            (r and 0xff shl 16) or
            (g and 0xff shl 8) or
            (b and 0xff),
    )

private fun LinearGradient.toBrush(density: Density): Brush =
    with(density) {
        Brush.linearGradient(
            colorStops = colorStops.map { it.percent.value.toFloat() to it.color.toComposeColor() }
                .toTypedArray(),
            start = Offset(
                x1.dp.toPx(),
                y1.dp.toPx(),
            ),
            end = Offset(
                x2.dp.toPx(),
                y2.dp.toPx(),
            ),
        )
    }

private fun RadialGradient.toBrush(density: Density): Brush =
    with(density) {
        Brush.radialGradient(
            colorStops = colorStops.map { it.percent.value.toFloat() to it.color.toComposeColor() }
                .toTypedArray(),
            radius = radius.dp.toPx(),
        )
    }

private typealias StrokeWidth = Double

private fun StrokeWidth.toStroke(density: Density): Stroke =
    with(density) {
        Stroke(width = dp.toPx())
    }

private fun Color.toColor(): Int =
    ((255 * this.alpha.value).toInt() and 0xff shl 24) or
        (this.r and 0xff shl 16) or
        (this.g and 0xff shl 8) or
        (this.b and 0xff)
