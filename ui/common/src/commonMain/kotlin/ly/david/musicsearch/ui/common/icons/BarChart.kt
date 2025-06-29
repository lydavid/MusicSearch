package ly.david.musicsearch.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val CustomIcons.BarChart: ImageVector
    get() {
        if (_BarChart != null) {
            return _BarChart!!
        }
        _BarChart = ImageVector.Builder(
            name = "BarChart",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(640f, 800f)
                verticalLineToRelative(-280f)
                horizontalLineToRelative(160f)
                verticalLineToRelative(280f)
                lineTo(640f, 800f)
                close()
                moveTo(400f, 800f)
                verticalLineToRelative(-640f)
                horizontalLineToRelative(160f)
                verticalLineToRelative(640f)
                lineTo(400f, 800f)
                close()
                moveTo(160f, 800f)
                verticalLineToRelative(-440f)
                horizontalLineToRelative(160f)
                verticalLineToRelative(440f)
                lineTo(160f, 800f)
                close()
            }
        }.build()

        return _BarChart!!
    }

@Suppress("ObjectPropertyName")
private var _BarChart: ImageVector? = null
