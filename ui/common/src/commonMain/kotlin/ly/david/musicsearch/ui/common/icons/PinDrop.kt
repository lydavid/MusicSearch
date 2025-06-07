package ly.david.musicsearch.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val CustomIcons.PinDrop: ImageVector
    get() {
        if (_PinDrop != null) {
            return _PinDrop!!
        }
        _PinDrop = ImageVector.Builder(
            name = "PinDrop",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(18f, 8f)
                curveToRelative(0f, -3.31f, -2.69f, -6f, -6f, -6f)
                reflectiveCurveTo(6f, 4.69f, 6f, 8f)
                curveToRelative(0f, 4.5f, 6f, 11f, 6f, 11f)
                reflectiveCurveToRelative(6f, -6.5f, 6f, -11f)
                close()
                moveTo(10f, 8f)
                curveToRelative(0f, -1.1f, 0.9f, -2f, 2f, -2f)
                reflectiveCurveToRelative(2f, 0.9f, 2f, 2f)
                reflectiveCurveToRelative(-0.89f, 2f, -2f, 2f)
                curveToRelative(-1.1f, 0f, -2f, -0.9f, -2f, -2f)
                close()
                moveTo(5f, 20f)
                verticalLineToRelative(2f)
                horizontalLineToRelative(14f)
                verticalLineToRelative(-2f)
                lineTo(5f, 20f)
                close()
            }
        }.build()

        return _PinDrop!!
    }

@Suppress("ObjectPropertyName")
private var _PinDrop: ImageVector? = null
