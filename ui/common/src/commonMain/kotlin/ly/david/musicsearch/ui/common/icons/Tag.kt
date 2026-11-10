package ly.david.musicsearch.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val CustomIcons.Tag: ImageVector
    get() {
        if (_Tag != null) {
            return _Tag!!
        }
        _Tag = ImageVector.Builder(
            name = "Tag",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(21.4f, 14.25f)
                lineTo(14.25f, 21.4f)
                quadToRelative(-0.3f, 0.3f, -0.67f, 0.45f)
                reflectiveQuadTo(12.83f, 22f)
                reflectiveQuadToRelative(-0.75f, -0.15f)
                reflectiveQuadTo(11.4f, 21.4f)
                lineTo(2.58f, 12.58f)
                quadTo(2.3f, 12.3f, 2.15f, 11.94f)
                reflectiveQuadTo(2f, 11.18f)
                verticalLineTo(4f)
                quadTo(2f, 3.17f, 2.59f, 2.59f)
                reflectiveQuadTo(4f, 2f)
                horizontalLineToRelative(7.18f)
                quadToRelative(0.4f, 0f, 0.78f, 0.16f)
                reflectiveQuadTo(12.6f, 2.6f)
                lineToRelative(8.8f, 8.83f)
                quadToRelative(0.3f, 0.3f, 0.44f, 0.68f)
                reflectiveQuadToRelative(0.14f, 0.75f)
                reflectiveQuadToRelative(-0.14f, 0.74f)
                reflectiveQuadTo(21.4f, 14.25f)
                close()
                moveTo(12.83f, 20f)
                lineToRelative(7.15f, -7.15f)
                lineTo(11.15f, 4f)
                horizontalLineTo(4f)
                verticalLineToRelative(7.15f)
                lineTo(12.83f, 20f)
                close()
                moveTo(6.5f, 8f)
                quadTo(7.13f, 8f, 7.56f, 7.56f)
                reflectiveQuadTo(8f, 6.5f)
                reflectiveQuadTo(7.56f, 5.44f)
                reflectiveQuadTo(6.5f, 5f)
                reflectiveQuadTo(5.44f, 5.44f)
                reflectiveQuadTo(5f, 6.5f)
                reflectiveQuadTo(5.44f, 7.56f)
                reflectiveQuadTo(6.5f, 8f)
                close()
                moveTo(12f, 12f)
                close()
            }
        }.build()

        return _Tag!!
    }

@Suppress("ObjectPropertyName")
private var _Tag: ImageVector? = null
