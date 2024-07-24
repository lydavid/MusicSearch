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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import io.data2viz.color.Color
import io.data2viz.color.ColorOrGradient
import io.data2viz.color.LinearGradient
import io.data2viz.color.RadialGradient
import io.data2viz.geom.Arc
import io.data2viz.geom.ArcTo
import io.data2viz.geom.BezierCurveTo
import io.data2viz.geom.ClosePath
import io.data2viz.geom.LineTo
import io.data2viz.geom.MoveTo
import io.data2viz.geom.QuadraticCurveTo
import io.data2viz.geom.RectCmd
import io.data2viz.math.toDegrees
import io.data2viz.viz.CircleNode
import io.data2viz.viz.FontPosture
import io.data2viz.viz.FontWeight
import io.data2viz.viz.LineNode
import io.data2viz.viz.PathNode
import io.data2viz.viz.RectNode
import io.data2viz.viz.TextHAlign
import io.data2viz.viz.TextNode
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sign
import kotlin.math.sin
import androidx.compose.ui.graphics.Color as ComposeColor

fun DrawScope.render(lineNode: LineNode) {
    lineNode.strokeColor?.let {
        val start = Offset(lineNode.x1.dp.toPx(), lineNode.y1.dp.toPx())
        val end = Offset(lineNode.x2.dp.toPx(), lineNode.y2.dp.toPx())
        when (it) {
            is Color -> drawLine(
                it.toComposeColor(),
                start,
                end,
                strokeWidth = lineNode.strokeWidth?.toFloat() ?: Stroke.HairlineWidth
            )
            is LinearGradient -> drawLine(
                it.toBrush(this@render),
                start,
                end,
                strokeWidth = lineNode.strokeWidth?.toFloat() ?: Stroke.HairlineWidth
            )
            is RadialGradient -> drawLine(
                it.toBrush(this@render),
                start,
                end,
                strokeWidth = lineNode.strokeWidth?.toFloat() ?: Stroke.HairlineWidth
            )
        }
    }
}

// TODO: https://issuetracker.google.com/issues/190787898
//fun DrawScope.render(node: TextNode) {
//    val paint = AndroidGraphicsPaint().apply {
//        textAlign = node.hAlign.android
//        textSize = node.fontSize.dp.toPx()
//        typeface = Typeface.create(
//            node.fontFamily.name,
//            getAndroidStyle(node.fontWeight, node.fontStyle)
//        )
//    }
//
//    node.textColor?.let {
//        paint.style = AndroidGraphicsPaint.Style.FILL
//        it.updatePaint(paint, this)
//        drawContext.canvas.nativeCanvas.drawText(
//            node.textContent,
//            node.x.dp.toPx(),
//            node.y.dp.toPx(),
//            paint
//        )
//    }
//    node.strokeColor?.let {
//        paint.style = AndroidGraphicsPaint.Style.STROKE
//        it.updatePaint(paint, this)
//        drawContext.canvas.nativeCanvas.drawText(
//            node.textContent,
//            node.x.dp.toPx(),
//            node.y.dp.toPx(),
//            paint
//        )
//    }
//}

//fun DrawScope.render(node: PathNode) {
//    val p = Path()
//    val epsilonFloat = 0.0001f
//    val epsilonCircle = 360 - epsilonFloat
//
//    fun Boolean.toSign(): Int = if (this) 1 else -1
//
//    fun arcTo(
//        lastX: Double,
//        lastY: Double,
//        cpX: Double,
//        cpY: Double,
//        x: Double,
//        y: Double,
//        r: Double
//    ) {
//        val alpha1 = atan2(lastY - cpY, lastX - cpX)
//        val alpha2 = atan2(y - cpY, x - cpX)
//        val alpha = angle(alpha1 - alpha2)
//        val d = r / sin(alpha / 2).absoluteValue
//        val cx = cpX + d * cos(alpha1 - alpha / 2)
//        val cy = cpY + d * sin(alpha1 - alpha / 2)
//
//        val clockwise = alpha > 0
//        val startAngle = alpha1.radToDeg.toFloat() + clockwise.toSign() * 90f
//        val sweepAngle =
//            if (clockwise) {
//                360 - ((180f + alpha.radToDeg.toFloat()) % 360f)
//            } else {
//                (-180f - alpha.radToDeg.toFloat()) % 360f
//            }
//
//        p.moveTo(lastX.dp.toPx(), lastY.dp.toPx())
//        val rect = Rect(
//            (cx - r).dp.toPx(),
//            (cy - r).dp.toPx(),
//            (cx + r).dp.toPx(),
//            (cy + r).dp.toPx()
//        )
//
//        p.arcTo(
//            rect,
//            startAngle,
//            sweepAngle,
//            false
//        )
//        p.lineTo(x.dp.toPx(), y.dp.toPx())
//    }
//
//    node.path.commands.forEachIndexed { index, cmd ->
//        when (cmd) {
//            is MoveTo -> p.moveTo(
//                cmd.x.dp.toPx(),
//                cmd.y.dp.toPx()
//            )
//            is LineTo -> p.lineTo(
//                cmd.x.dp.toPx(),
//                cmd.y.dp.toPx()
//            )
//            is QuadraticCurveTo -> p.quadraticBezierTo(
//                cmd.cpx.dp.toPx(),
//                cmd.cpy.dp.toPx(),
//                cmd.x.dp.toPx(),
//                cmd.y.dp.toPx()
//            )
//            is BezierCurveTo -> p.cubicTo(
//                cmd.cpx1.dp.toPx(),
//                cmd.cpy1.dp.toPx(),
//                cmd.cpx2.dp.toPx(),
//                cmd.cpy2.dp.toPx(),
//                cmd.x.dp.toPx(),
//                cmd.y.dp.toPx()
//            )
//            is ArcTo -> {
//                val prev = node.path.commands[index - 1]
//                arcTo(prev.x, prev.y, cmd.fromX, cmd.fromY, cmd.x, cmd.y, cmd.radius)
//            }
//            is Arc -> {
//                val r = cmd.radius
//
//                val rect = Rect(
//                    (cmd.centerX - r).dp.toPx(),
//                    (cmd.centerY - r).dp.toPx(),
//                    (cmd.centerX + r).dp.toPx(),
//                    (cmd.centerY + r).dp.toPx()
//                )
//
//                val startAngle = cmd.startAngle.toDegrees().toFloat()
//                var sweepAngle = cmd.endAngle.toDegrees().toFloat() - startAngle
//
//                // translating the rotation to an angle value
//                if (!cmd.counterClockWise && sweepAngle < -epsilonFloat) sweepAngle =
//                    (sweepAngle % 360) + 360
//                if (cmd.counterClockWise && sweepAngle > epsilonFloat) sweepAngle =
//                    (sweepAngle % 360) - 360
//
//                // on Android, an arc with an angle of 360 is not drawn
//                if (abs(sweepAngle) >= epsilonCircle && abs(sweepAngle % 360) <= epsilonFloat) {
//                    sweepAngle = epsilonCircle * sweepAngle.sign
//                }
//
//                p.arcTo(rect, startAngle, sweepAngle, false)
//            }
//            is RectCmd -> p.addRect(
//                Rect(
//                    cmd.x.dp.toPx(),
//                    cmd.y.dp.toPx(),
//                    (cmd.x + cmd.w).dp.toPx(),
//                    (cmd.y + cmd.h).dp.toPx()
//                )
//            )
//            is ClosePath -> p.close()
//            else -> error("Unknown path command:: ${cmd::class}")
//        }
//    }
//
//    node.fill?.let { fill ->
//        when (fill) {
//            is Color -> drawPath(p, color = fill.toComposeColor())
//            is LinearGradient -> drawPath(p, fill.toBrush(this@render))
//            is RadialGradient -> drawPath(p, fill.toBrush(this@render))
//        }
//    }
//
//    if (node.strokeWidth != null && node.strokeColor != null) {
//        val stroke = node.strokeWidth?.toStroke(this)
//            ?: Stroke()
//        node.strokeColor?.let { colorOrGradient ->
//            when (colorOrGradient) {
//                is Color -> drawPath(
//                    p,
//                    color = colorOrGradient.toComposeColor(),
//                    style = stroke
//                )
//                is LinearGradient -> drawPath(
//                    p,
//                    colorOrGradient.toBrush(this@render),
//                    style = stroke
//                )
//                is RadialGradient -> drawPath(
//                    p,
//                    colorOrGradient.toBrush(this@render),
//                    style = stroke
//                )
//            }
//        }
//    }
//}

fun DrawScope.render(node: RectNode) {
    node.fill?.let {
        render(node, it, Fill)
    }
    node.strokeColor?.let { colorOrGradient ->
        node.strokeWidth?.let {
            val stroke = node.strokeWidth?.toStroke(this@render) ?: Stroke()
            render(node, colorOrGradient, stroke)
        }
    }
}

private fun DrawScope.render(
    node: RectNode,
    colorOrGradient: ColorOrGradient,
    style: DrawStyle
) {
    val topLeft = Offset(node.x.dp.toPx(), node.y.dp.toPx())
    val s = Size(node.width.dp.toPx(), node.height.dp.toPx())
    when (colorOrGradient) {
        is Color -> drawRect(
            colorOrGradient.toComposeColor(),
            topLeft,
            s,
            style = style
        )
        is LinearGradient -> drawRect(
            colorOrGradient.toBrush(this),
            topLeft,
            s,
            style = style
        )
        is RadialGradient -> drawRect(
            colorOrGradient.toBrush(this),
            topLeft,
            s,
            style = style
        )
    }
}

fun DrawScope.render(
    node: CircleNode,
    offset: Offset,
) {
    with(node) {
        val r = radius.dp.toPx()
        val c = Offset(x.dp.toPx(), y.dp.toPx()) + offset
        fill?.let {
            when (it) {
                is Color -> drawCircle(
                    color = it.toComposeColor(),
                    radius = r,
                    center = c
                )
                is LinearGradient -> drawCircle(
                    brush = it.toBrush(this@render),
                    radius = r,
                    center = c
                )
                is RadialGradient -> drawCircle(
                    brush = it.toBrush(this@render),
                    radius = r,
                    center = c
                )
            }
        }

        strokeColor?.let {
            val stroke = node.strokeWidth?.toStroke(this@render)
                ?: Stroke()
            when (it) {
                is Color -> drawCircle(
                    color = it.toComposeColor(),
                    radius = r,
                    center = c,
                    style = stroke
                )
                is LinearGradient -> drawCircle(
                    brush = it.toBrush(this@render),
                    radius = r,
                    center = c,
                    style = stroke
                )
                is RadialGradient -> drawCircle(
                    brush = it.toBrush(this@render),
                    radius = r,
                    center = c,
                    style = stroke
                )
            }
        }
    }
}

private fun Color.toComposeColor(): ComposeColor =
    ComposeColor(
        (255 * alpha.value).toInt() and 0xff shl 24 or
                (r and 0xff shl 16) or
                (g and 0xff shl 8) or
                (b and 0xff)
    )

private fun LinearGradient.toBrush(density: Density): Brush =
    with(density) {
        Brush.linearGradient(
            colorStops = colorStops.map { it.percent.value.toFloat() to it.color.toComposeColor() }
                .toTypedArray(),
            start = Offset(x1.dp.toPx(), y1.dp.toPx()),
            end = Offset(x2.dp.toPx(), y2.dp.toPx())
        )
    }

private fun RadialGradient.toBrush(density: Density): Brush =
    with(density) {
        Brush.radialGradient(
            colorStops = colorStops.map { it.percent.value.toFloat() to it.color.toComposeColor() }
                .toTypedArray(),
            radius = radius.dp.toPx()
        )
    }

private typealias StrokeWidth = Double

private fun StrokeWidth.toStroke(density: Density): Stroke =
    with(density) {
        Stroke(width = dp.toPx())
    }

//private val TextHAlign.android: AndroidGraphicsPaint.Align
//    get() = when (this) {
//        TextHAlign.START, TextHAlign.LEFT -> AndroidGraphicsPaint.Align.LEFT
//        TextHAlign.END, TextHAlign.RIGHT -> AndroidGraphicsPaint.Align.RIGHT
//        TextHAlign.MIDDLE -> AndroidGraphicsPaint.Align.CENTER
//    }
//
//private fun getAndroidStyle(fontWeight: FontWeight, fontStyle: FontPosture): Int {
//    return when (fontWeight) {
//        FontWeight.NORMAL ->
//            when (fontStyle) {
//                FontPosture.NORMAL -> Typeface.NORMAL
//                FontPosture.ITALIC -> Typeface.ITALIC
//            }
//
//        FontWeight.BOLD ->
//            when (fontStyle) {
//                FontPosture.NORMAL -> Typeface.BOLD
//                FontPosture.ITALIC -> Typeface.BOLD_ITALIC
//            }
//    }
//}
//
//private typealias ALinearGradient = android.graphics.LinearGradient
//private typealias ARadialGradient = android.graphics.RadialGradient
//
//private fun ColorOrGradient.updatePaint(
//    paint: AndroidGraphicsPaint,
//    density: Density
//) {
//    when (this) {
//        is Color -> {
//            paint.color = toColor()
//            paint.shader = null
//        }
//        is LinearGradient -> paint.shader = toLinearGradient(density)
//        is RadialGradient -> paint.shader = toRadialGradient(density)
//        else -> error("Unknown type :: ${this::class}")
//    }
//}
//
//private fun RadialGradient.toRadialGradient(density: Density) =
//    with(density) {
//        ARadialGradient(
//            cx.dp.toPx(),
//            cy.dp.toPx(),
//            radius.dp.toPx(),
//            IntArray(colorStops.size) { colorStops[it].color.toColor() },
//            FloatArray(colorStops.size) { colorStops[it].percent.value.toFloat() },
//            Shader.TileMode.CLAMP
//        )
//    }
//
//private fun LinearGradient.toLinearGradient(density: Density) =
//    with(density) {
//        ALinearGradient(
//            x1.dp.toPx(),
//            y1.dp.toPx(),
//            x2.dp.toPx(),
//            y2.dp.toPx(),
//            IntArray(colorStops.size) { colorStops[it].color.toColor() },
//            FloatArray(colorStops.size) { colorStops[it].percent.value.toFloat() },
//            Shader.TileMode.CLAMP
//        )
//    }

private fun Color.toColor(): Int =
    ((255 * this.alpha.value).toInt() and 0xff shl 24) or
            (this.r and 0xff shl 16) or
            (this.g and 0xff shl 8) or
            (this.b and 0xff)
