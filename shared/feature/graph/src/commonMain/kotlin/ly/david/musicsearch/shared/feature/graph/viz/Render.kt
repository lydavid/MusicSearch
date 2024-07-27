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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.feature.graph.GraphLink
import ly.david.musicsearch.shared.feature.graph.GraphNode

fun DrawScope.render(
    lineNode: GraphLink,
    offset: Offset,
    color: Color,
) {
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
        strokeWidth = 1f,
    )
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
            color = entity.getNodeColor(),
            radius = r,
            center = c,
        )
    }
}

internal fun MusicBrainzEntity.getNodeColor(): Color {
    return when (this) {
        MusicBrainzEntity.AREA -> Color(0xFF4CAF50)
        MusicBrainzEntity.ARTIST -> Color(0xFFFF5722)
        MusicBrainzEntity.COLLECTION -> Color(0xFF9C27B0)
        MusicBrainzEntity.EVENT -> Color(0xFFFFC107)
        MusicBrainzEntity.GENRE -> Color(0xFF2196F3)
        MusicBrainzEntity.INSTRUMENT -> Color(0xFF795548)
        MusicBrainzEntity.LABEL -> Color(0xFFF44336)
        MusicBrainzEntity.PLACE -> Color(0xFF009688)
        MusicBrainzEntity.RECORDING -> Color(0xFFE91E63)
        MusicBrainzEntity.RELEASE -> Color(0xFF3F51B5)
        MusicBrainzEntity.RELEASE_GROUP -> Color(0xFF8BC34A)
        MusicBrainzEntity.SERIES -> Color(0xFFFF9800)
        MusicBrainzEntity.WORK -> Color(0xFF607D8B)
        MusicBrainzEntity.URL -> Color(0xFF00BCD4)
    }
}
