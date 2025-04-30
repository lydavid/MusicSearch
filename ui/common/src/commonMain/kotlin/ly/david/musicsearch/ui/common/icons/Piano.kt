package ly.david.musicsearch.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val CustomIcons.Piano: ImageVector
    get() {
        if (_Piano != null) {
            return _Piano!!
        }
        _Piano = ImageVector.Builder(
            name = "Piano",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(200f, 840f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(120f, 760f)
                verticalLineToRelative(-560f)
                quadToRelative(0f, -33f, 23.5f, -56.5f)
                reflectiveQuadTo(200f, 120f)
                horizontalLineToRelative(560f)
                quadToRelative(33f, 0f, 56.5f, 23.5f)
                reflectiveQuadTo(840f, 200f)
                verticalLineToRelative(560f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                reflectiveQuadTo(760f, 840f)
                lineTo(200f, 840f)
                close()
                moveTo(200f, 760f)
                horizontalLineToRelative(130f)
                verticalLineToRelative(-180f)
                horizontalLineToRelative(-10f)
                quadToRelative(-17f, 0f, -28.5f, -11.5f)
                reflectiveQuadTo(280f, 540f)
                verticalLineToRelative(-340f)
                horizontalLineToRelative(-80f)
                verticalLineToRelative(560f)
                close()
                moveTo(630f, 760f)
                horizontalLineToRelative(130f)
                verticalLineToRelative(-560f)
                horizontalLineToRelative(-80f)
                verticalLineToRelative(340f)
                quadToRelative(0f, 17f, -11.5f, 28.5f)
                reflectiveQuadTo(640f, 580f)
                horizontalLineToRelative(-10f)
                verticalLineToRelative(180f)
                close()
                moveTo(390f, 760f)
                horizontalLineToRelative(180f)
                verticalLineToRelative(-180f)
                horizontalLineToRelative(-10f)
                quadToRelative(-17f, 0f, -28.5f, -11.5f)
                reflectiveQuadTo(520f, 540f)
                verticalLineToRelative(-340f)
                horizontalLineToRelative(-80f)
                verticalLineToRelative(340f)
                quadToRelative(0f, 17f, -11.5f, 28.5f)
                reflectiveQuadTo(400f, 580f)
                horizontalLineToRelative(-10f)
                verticalLineToRelative(180f)
                close()
            }
        }.build()

        return _Piano!!
    }

@Suppress("ObjectPropertyName")
private var _Piano: ImageVector? = null
