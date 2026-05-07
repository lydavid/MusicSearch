package ly.david.musicsearch.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val CustomIcons.ExpandAll: ImageVector
    get() {
        if (_ExpandAll != null) {
            return _ExpandAll!!
        }
        _ExpandAll = ImageVector.Builder(
            name = "ExpandAll",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(480f, 880f)
                lineTo(240f, 640f)
                lineToRelative(57f, -57f)
                lineToRelative(183f, 183f)
                lineToRelative(183f, -183f)
                lineToRelative(57f, 57f)
                lineTo(480f, 880f)
                close()
                moveTo(298f, 376f)
                lineToRelative(-58f, -56f)
                lineToRelative(240f, -240f)
                lineToRelative(240f, 240f)
                lineToRelative(-58f, 56f)
                lineToRelative(-182f, -182f)
                lineToRelative(-182f, 182f)
                close()
            }
        }.build()

        return _ExpandAll!!
    }

@Suppress("ObjectPropertyName")
private var _ExpandAll: ImageVector? = null
