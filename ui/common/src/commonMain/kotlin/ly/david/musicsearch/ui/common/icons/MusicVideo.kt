package ly.david.musicsearch.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val CustomIcons.MusicVideo: ImageVector
    get() {
        if (_MusicVideo != null) {
            return _MusicVideo!!
        }
        _MusicVideo = ImageVector.Builder(
            name = "MusicVideo",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(420f, 680f)
                quadToRelative(42f, 0f, 71f, -29f)
                reflectiveQuadToRelative(29f, -71f)
                verticalLineToRelative(-220f)
                horizontalLineToRelative(120f)
                verticalLineToRelative(-80f)
                lineTo(480f, 280f)
                verticalLineToRelative(220f)
                quadToRelative(-13f, -9f, -28f, -14.5f)
                reflectiveQuadToRelative(-32f, -5.5f)
                quadToRelative(-42f, 0f, -71f, 29f)
                reflectiveQuadToRelative(-29f, 71f)
                quadToRelative(0f, 42f, 29f, 71f)
                reflectiveQuadToRelative(71f, 29f)
                close()
                moveTo(160f, 800f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(80f, 720f)
                verticalLineToRelative(-480f)
                quadToRelative(0f, -33f, 23.5f, -56.5f)
                reflectiveQuadTo(160f, 160f)
                horizontalLineToRelative(640f)
                quadToRelative(33f, 0f, 56.5f, 23.5f)
                reflectiveQuadTo(880f, 240f)
                verticalLineToRelative(480f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                reflectiveQuadTo(800f, 800f)
                lineTo(160f, 800f)
                close()
                moveTo(160f, 720f)
                horizontalLineToRelative(640f)
                verticalLineToRelative(-480f)
                lineTo(160f, 240f)
                verticalLineToRelative(480f)
                close()
                moveTo(160f, 720f)
                verticalLineToRelative(-480f)
                verticalLineToRelative(480f)
                close()
            }
        }.build()

        return _MusicVideo!!
    }

@Suppress("ObjectPropertyName")
private var _MusicVideo: ImageVector? = null
