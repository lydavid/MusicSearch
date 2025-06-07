package ly.david.musicsearch.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val CustomIcons.PhotoLibrary: ImageVector
    get() {
        if (_PhotoLibrary != null) {
            return _PhotoLibrary!!
        }
        _PhotoLibrary = ImageVector.Builder(
            name = "PhotoLibrary",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(20f, 4f)
                verticalLineToRelative(12f)
                lineTo(8f, 16f)
                lineTo(8f, 4f)
                horizontalLineToRelative(12f)
                moveToRelative(0f, -2f)
                lineTo(8f, 2f)
                curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
                verticalLineToRelative(12f)
                curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
                horizontalLineToRelative(12f)
                curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
                lineTo(22f, 4f)
                curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
                close()
                moveTo(11.5f, 11.67f)
                lineToRelative(1.69f, 2.26f)
                lineToRelative(2.48f, -3.1f)
                lineTo(19f, 15f)
                lineTo(9f, 15f)
                close()
                moveTo(2f, 6f)
                verticalLineToRelative(14f)
                curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
                horizontalLineToRelative(14f)
                verticalLineToRelative(-2f)
                lineTo(4f, 20f)
                lineTo(4f, 6f)
                lineTo(2f, 6f)
                close()
            }
        }.build()

        return _PhotoLibrary!!
    }

@Suppress("ObjectPropertyName")
private var _PhotoLibrary: ImageVector? = null
